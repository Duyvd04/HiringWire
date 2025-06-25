package com.hiringwire.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.hiringwire.mapper.ApplicantMapper;
import com.hiringwire.mapper.JobMapper;
import com.hiringwire.model.User;
import com.hiringwire.model.request.ApplicantRequest;
import com.hiringwire.model.request.JobRequest;
import com.hiringwire.model.request.NotificationRequest;
import com.hiringwire.model.response.JobResponse;
import com.hiringwire.repository.UserRepository;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.hiringwire.dto.Application;
import com.hiringwire.model.ApplicationStatus;
import com.hiringwire.model.enums.JobStatus;
import com.hiringwire.model.Applicant;
import com.hiringwire.model.Job;
import com.hiringwire.exception.HiringWireException;
import com.hiringwire.repository.JobRepository;
import org.springframework.transaction.annotation.Transactional;

@Service("jobService")
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {
	private final JobRepository IJobRepository;
	private final NotificationService notificationService;
	private final JavaMailSender mailSender;
	private final UserRepository userRepository;
	private final ResumeParser resumeParser;
	private final PdfGeneratorService pdfGeneratorService;
	private final JobMapper jobMapper;
	private final ApplicantMapper applicantMapper;

	@Override
//	@CachePut(value = "jobs", key = "#result.id", condition = "#result != null")
	public JobResponse postJob(JobRequest jobRequest) throws HiringWireException {
		if (jobRequest.getId() == null || jobRequest.getId() == 0) {
			Job job = jobMapper.toEntity(jobRequest);
			job.setPostTime(LocalDateTime.now());
			job.setJobStatus(JobStatus.PENDING);

			NotificationRequest notiDto = new NotificationRequest();
			notiDto.setAction("Job Posted");
			notiDto.setMessage("Job Posted Successfully for " + job.getJobTitle() + " at " + job.getCompany());
			notiDto.setUserId(job.getPostedBy());

			Job savedJob = IJobRepository.save(job);
			notiDto.setRoute("/posted-jobs/" + savedJob.getId());
			notificationService.sendNotification(notiDto);
			return jobMapper.toResponse(savedJob);
		} else {
			Job existing = IJobRepository.findById(jobRequest.getId())
					.orElseThrow(() -> new HiringWireException("JOB_NOT_FOUND"));

			if (existing.getJobStatus() == JobStatus.DRAFT || jobRequest.getJobStatus() == JobStatus.CLOSED) {
				existing.setPostTime(LocalDateTime.now());
			}

			Job updated = jobMapper.toEntity(jobRequest);
			updated.setId(existing.getId());
			return jobMapper.toResponse(IJobRepository.save(updated));
		}
	}
	@Override
//	@Cacheable(value = "jobsList", unless = "#result.isEmpty()")
	public List<JobResponse> getAllJobs() throws HiringWireException {
		List<Job> jobs = IJobRepository.findAll();
		if (jobs.isEmpty()) {
			throw new HiringWireException("NO_JOBS_FOUND");
		}
		return jobs.stream().map(jobMapper::toResponse).collect(Collectors.toList());
	}

	@Override
//	@Cacheable(value = "jobs", key = "#id")
	public JobResponse getJob(Long id) throws HiringWireException {
		Job job = IJobRepository.findById(id)
				.orElseThrow(() -> new HiringWireException("JOB_NOT_FOUND"));
		return jobMapper.toResponse(job);
	}

	@Override
	@Transactional
//	@CacheEvict(value = {"jobs", "jobsList", "history", "postedJobs", "pendingJobs"}, allEntries = true)
	public void applyJob(Long id, ApplicantRequest applicantRequest) throws HiringWireException {
		Job job = IJobRepository.findById(id)
				.orElseThrow(() -> new HiringWireException("JOB_NOT_FOUND"));

		User user = userRepository.findById(applicantRequest.getApplicantId())
				.orElseThrow(() -> new HiringWireException("USER_NOT_FOUND"));

		List<Applicant> applicants = job.getApplicants();
		if (applicants == null) {
			applicants = new ArrayList<>();
		}

		if (applicants.stream().anyMatch(x -> x.getUser().getId().equals(applicantRequest.getApplicantId()))) {
			throw new HiringWireException("JOB_APPLIED_ALREADY");
		}

		if (applicantRequest.getResume() != null) {
			try {
				byte[] resumeData = Base64.getDecoder().decode(applicantRequest.getResume());
				Map<String, Object> parsedInfo = resumeParser.parseResume(resumeData);
				byte[] extractedPdf = pdfGeneratorService.generateExtractedPdf(parsedInfo);
				applicantRequest.setExtractedResume(Base64.getEncoder().encodeToString(extractedPdf));

				// Set parsed fields
				applicantRequest.setName((String) parsedInfo.get("name"));
				applicantRequest.setEmail((String) parsedInfo.get("email"));
				applicantRequest.setPhone((String) parsedInfo.get("phone"));
			} catch (Exception e) {
				throw new HiringWireException("Failed to process resume: " + e.getMessage());
			}
		}

		Applicant applicant = applicantMapper.toEntity(applicantRequest);
		applicant.setUser(user);
		applicant.setPhone(applicantRequest.getPhone());
		applicant.setWebsite(applicantRequest.getWebsite());
		applicant.setResume(applicantRequest.getResume() != null ?
				Base64.getDecoder().decode(applicantRequest.getResume()) : null);
		applicant.setExtractedResume(applicantRequest.getExtractedResume() != null ?
				Base64.getDecoder().decode(applicantRequest.getExtractedResume()) : null);
		applicant.setCoverLetter(applicantRequest.getCoverLetter());
		applicant.setApplicationStatus(ApplicationStatus.APPLIED);
		applicant.setTimestamp(LocalDateTime.now());
		applicant.setJob(job);

		applicants.add(applicant);
		job.setApplicants(applicants);
		IJobRepository.save(job);

		sendApplicationEmail(user.getEmail(), job);
	}
	@Override
//	@Cacheable(value = "history", key = "#id + '-' + #applicationStatus")
	public List<JobResponse> getHistory(Long id, ApplicationStatus applicationStatus) {
		return IJobRepository.findByApplicantIdAndApplicationStatus(id, applicationStatus)
				.stream().map(jobMapper::toResponse).toList();
	}

	@Override
//	@Cacheable(value = "postedJobs", key = "#id")
	public List<JobResponse> getJobsPostedBy(Long id) throws HiringWireException {
		return IJobRepository.findByPostedBy(id).stream().map(jobMapper::toResponse).toList();
	}


	@Override
	@Transactional
	public void changeAppStatus(Application application) throws HiringWireException {
		Job job = IJobRepository.findById(application.getId())
				.orElseThrow(() -> new HiringWireException("JOB_NOT_FOUND"));

		boolean found = false;
		for (Applicant applicant : job.getApplicants()) {
			if (applicant.getUser().getId().equals(application.getApplicantId())) {
				found = true;
				applicant.setApplicationStatus(application.getApplicationStatus());

				if (application.getApplicationStatus() == ApplicationStatus.INTERVIEWING) {
					applicant.setInterviewTime(application.getInterviewTime());
					sendNotifications(application, job);
				}

				try {
					User applicantUser = applicant.getUser(); // Use the User relationship
					sendStatusUpdateEmail(applicantUser.getEmail(), job, application.getApplicationStatus());
				} catch (Exception e) {
					throw new HiringWireException("Failed to send notification: " + e.getMessage());
				}
				break;
			}
		}

		if (!found) {
			throw new HiringWireException("APPLICANT_NOT_FOUND");
		}

		IJobRepository.save(job);
	}

	// ... (other methods remain unchanged)



	@Override
	@CacheEvict(value = {"jobs", "jobsList", "history", "postedJobs", "pendingJobs"}, key = "#id")
	public void deleteJob(Long id) throws HiringWireException {
		IJobRepository.deleteById(id);
	}
	private void sendNotifications(Application application, Job job) {
		NotificationRequest notiDto = new NotificationRequest();
		notiDto.setAction("Interview Scheduled");
		notiDto.setMessage("Interview scheduled for job id: " + application.getId());
		notiDto.setUserId(application.getApplicantId());
		notiDto.setRoute("/job-history");
		try {
			notificationService.sendNotification(notiDto);
		} catch (HiringWireException e) {
			e.printStackTrace();
		}
	}
	private void sendApplicationEmail(String email, Job job) throws HiringWireException {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(new InternetAddress("vuducduy1112004@gmail.com", "HiringWire"));
			helper.setTo(email);
			helper.setSubject("Job Application Confirmation");
			String htmlContent = String.format("""
                <html>
                <body>
                    <h2>Application Confirmation</h2>
                    <p>Your application for the position of <strong>%s</strong> at <strong>%s</strong> has been received.</p>
                    <p>We will review your application and get back to you soon.</p>
                    <p>Thank you for your interest!</p>
                </body>
                </html>
                """, job.getJobTitle(), job.getCompany());
			helper.setText(htmlContent, true);
			mailSender.send(message);
		} catch (Exception e) {
			throw new HiringWireException("Failed to send application confirmation email: " + e.getMessage());
		}
	}

	private void sendStatusUpdateEmail(String email, Job job, ApplicationStatus status) throws HiringWireException {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(new InternetAddress("vuducduy1112004@gmail.com", "HiringWire"));
			helper.setTo(email);
			helper.setSubject("Application Status Update");
			String htmlContent = String.format("""
                <html>
                <body>
                    <h2>Application Status Update</h2>
                    <p>Your application for the position of <strong>%s</strong> at <strong>%s</strong> has been updated.</p>
                    <p>New status: <strong>%s</strong></p>
                    %s
                </body>
                </html>
                """,
					job.getJobTitle(),
					job.getCompany(),
					status,
					status == ApplicationStatus.INTERVIEWING ?
							"<p>Please check your notifications for interview details.</p>" : "");
			helper.setText(htmlContent, true);
			mailSender.send(message);
		} catch (Exception e) {
			throw new HiringWireException("Failed to send status update email: " + e.getMessage());
		}
	}
	@Override
	public Job getJobWithApplicant(Long jobId, Long applicantId) throws HiringWireException {
		return IJobRepository.findById(jobId)
				.orElseThrow(() -> new HiringWireException("JOB_NOT_FOUND"));
	}
	@Override
//	@Cacheable(value = "pendingJobs")
	public List<JobResponse> getPendingJobs() throws HiringWireException {
		List<Job> jobs = IJobRepository.findByJobStatus(JobStatus.PENDING);
		return jobs.stream().map(jobMapper::toResponse)
				.collect(Collectors.toList());
	}
	@Override
	@Transactional
//	@CacheEvict(value = {"jobs", "jobsList", "history", "postedJobs", "pendingJobs"}, key = "#id")
	public void approveJob(Long id) throws HiringWireException {
		Job job = IJobRepository.findById(id)
				.orElseThrow(() -> new HiringWireException("JOB_NOT_FOUND"));

		job.setJobStatus(JobStatus.ACTIVE);
		IJobRepository.save(job);

		NotificationRequest notification = new NotificationRequest();
		notification.setUserId(job.getPostedBy());
		notification.setAction("Job Approved");
		notification.setMessage("Your job posting for " + job.getJobTitle() + " has been approved");
		notification.setRoute("/posted-jobs/" + job.getId());
		notificationService.sendNotification(notification);
	}

	@Override
	@Transactional
//	@CacheEvict(value = {"jobs", "jobsList", "history", "postedJobs", "pendingJobs"}, key = "#id")
	public void rejectJob(Long id) throws HiringWireException {
		Job job = IJobRepository.findById(id)
				.orElseThrow(() -> new HiringWireException("JOB_NOT_FOUND"));

		job.setJobStatus(JobStatus.REJECTED);
		IJobRepository.save(job);

		NotificationRequest notification = new NotificationRequest();
		notification.setUserId(job.getPostedBy());
		notification.setAction("Job Rejected");
		notification.setMessage("Your job posting for " + job.getJobTitle() + " has been rejected");
		notification.setRoute("/posted-jobs/" + job.getId());
		notificationService.sendNotification(notification);
	}
}
