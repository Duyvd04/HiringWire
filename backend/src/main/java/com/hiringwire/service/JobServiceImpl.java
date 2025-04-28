package com.hiringwire.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.hiringwire.entity.User;
import com.hiringwire.repository.IUserRepository;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.hiringwire.dto.ApplicantDTO;
import com.hiringwire.dto.Application;
import com.hiringwire.dto.ApplicationStatus;
import com.hiringwire.dto.JobDTO;
import com.hiringwire.dto.JobStatus;
import com.hiringwire.dto.NotificationDTO;
import com.hiringwire.entity.Applicant;
import com.hiringwire.entity.Job;
import com.hiringwire.exception.HiringWireException;
import com.hiringwire.repository.IJobRepository;
import org.springframework.transaction.annotation.Transactional;

@Service("jobService")
public class JobServiceImpl implements JobService {

	@Autowired
	private IJobRepository IJobRepository;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private ResumeParser resumeParser;

	@Autowired
	private PdfGeneratorService pdfGeneratorService;

	@Override
	public JobDTO postJob(JobDTO jobDTO) throws HiringWireException {
		if (jobDTO.getId() == null || jobDTO.getId() == 0) {
			jobDTO.setPostTime(LocalDateTime.now());
			jobDTO.setJobStatus(JobStatus.PENDING);

			NotificationDTO notiDto = new NotificationDTO();
			notiDto.setAction("Job Posted");
			notiDto.setMessage("Job Posted Successfully for " + jobDTO.getJobTitle() + " at " + jobDTO.getCompany());
			notiDto.setUserId(jobDTO.getPostedBy());

			Job savedJob = IJobRepository.save(jobDTO.toEntity());
			notiDto.setRoute("/posted-jobs/" + savedJob.getId());
			notificationService.sendNotification(notiDto);
			return savedJob.toDTO();
		} else {
			Job job = IJobRepository.findById(jobDTO.getId())
					.orElseThrow(() -> new HiringWireException("JOB_NOT_FOUND"));
			if (job.getJobStatus().equals(JobStatus.DRAFT) || jobDTO.getJobStatus().equals(JobStatus.CLOSED))
				jobDTO.setPostTime(LocalDateTime.now());
			return IJobRepository.save(jobDTO.toEntity()).toDTO();
		}
	}

	@Override
	public List<JobDTO> getAllJobs() throws HiringWireException {
		List<Job> jobs = IJobRepository.findAll();
		if (jobs.isEmpty()) {
			throw new HiringWireException("NO_JOBS_FOUND");
		}
		return jobs.stream().map(Job::toDTO).collect(Collectors.toList());
	}

	@Override
	public JobDTO getJob(Long id) throws HiringWireException {
		return IJobRepository.findById(id).orElseThrow(() -> new HiringWireException("JOB_NOT_FOUND")).toDTO();
	}

	@Override
	@Transactional
	public void applyJob(Long id, ApplicantDTO applicantDTO) throws HiringWireException {
		Job job = IJobRepository.findById(id)
				.orElseThrow(() -> new HiringWireException("JOB_NOT_FOUND"));

		User user = userRepository.findById(applicantDTO.getApplicantId())
				.orElseThrow(() -> new HiringWireException("USER_NOT_FOUND"));

		List<Applicant> applicants = job.getApplicants();
		if (applicants == null) {
			applicants = new ArrayList<>();
		}

		// Check if user has already applied
		if (applicants.stream().anyMatch(x -> x.getApplicantId().equals(applicantDTO.getApplicantId()))) {
			throw new HiringWireException("JOB_APPLIED_ALREADY");
		}

		// Process resume if provided
		if (applicantDTO.getResume() != null) {
			try {
				byte[] resumeData = Base64.getDecoder().decode(applicantDTO.getResume());
				Map<String, Object> parsedInfo = resumeParser.parseResume(resumeData);
				byte[] extractedPdf = pdfGeneratorService.generateExtractedPdf(parsedInfo);
				applicantDTO.setExtractedResume(Base64.getEncoder().encodeToString(extractedPdf));
			} catch (IOException e) {
				throw new HiringWireException("Failed to process resume: " + e.getMessage());
			}
		}

		// Create new Applicant
		Applicant applicant = new Applicant();
		applicant.setApplicantId(applicantDTO.getApplicantId());
		applicant.setName(user.getName());
		applicant.setEmail(user.getEmail());
		applicant.setPhone(applicantDTO.getPhone());
		applicant.setWebsite(applicantDTO.getWebsite());
		applicant.setResume(applicantDTO.getResume() != null ?
				Base64.getDecoder().decode(applicantDTO.getResume()) : null);
		applicant.setExtractedResume(applicantDTO.getExtractedResume() != null ?
				Base64.getDecoder().decode(applicantDTO.getExtractedResume()) : null);
		applicant.setCoverLetter(applicantDTO.getCoverLetter());
		applicant.setApplicationStatus(ApplicationStatus.APPLIED);
		applicant.setTimestamp(LocalDateTime.now());

		// Add to applicants list and save
		applicants.add(applicant);
		job.setApplicants(applicants);
		IJobRepository.save(job);

		sendApplicationEmail(user.getEmail(), job);
	}
	@Override
	public List<JobDTO> getHistory(Long id, ApplicationStatus applicationStatus) {
		return IJobRepository.findByApplicantIdAndApplicationStatus(id, applicationStatus)
				.stream().map((x) -> x.toDTO()).toList();
	}

	@Override
	public List<JobDTO> getJobsPostedBy(Long id) throws HiringWireException {
		return IJobRepository.findByPostedBy(id).stream().map((x) -> x.toDTO()).toList();
	}


	@Override
	@Transactional
	public void changeAppStatus(Application application) throws HiringWireException {
		Job job = IJobRepository.findById(application.getId())
				.orElseThrow(() -> new HiringWireException("JOB_NOT_FOUND"));

		boolean found = false;
		for (Applicant applicant : job.getApplicants()) {
			if (applicant.getApplicantId().equals(application.getApplicantId())) {
				found = true;
				applicant.setApplicationStatus(application.getApplicationStatus());

				if (application.getApplicationStatus() == ApplicationStatus.INTERVIEWING) {
					applicant.setInterviewTime(application.getInterviewTime());
					sendNotifications(application, job);
				}

				try {
					User applicantUser = userRepository.findById(applicant.getApplicantId())
							.orElseThrow(() -> new HiringWireException("USER_NOT_FOUND"));
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

	@Override
	public void deleteJob(Long id) throws HiringWireException {
		IJobRepository.deleteById(id);
	}
	private void sendNotifications(Application application, Job job) {
		NotificationDTO notiDto = new NotificationDTO();
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
	public List<JobDTO> getPendingJobs() throws HiringWireException {
		List<Job> jobs = IJobRepository.findByJobStatus(JobStatus.PENDING);
		return jobs.stream()
				.map(Job::toDTO)
				.collect(Collectors.toList());
	}
	@Override
	@Transactional
	public void approveJob(Long id) throws HiringWireException {
		Job job = IJobRepository.findById(id)
				.orElseThrow(() -> new HiringWireException("JOB_NOT_FOUND"));

		job.setJobStatus(JobStatus.ACTIVE);
		IJobRepository.save(job);

		NotificationDTO notification = new NotificationDTO();
		notification.setUserId(job.getPostedBy());
		notification.setAction("Job Approved");
		notification.setMessage("Your job posting for " + job.getJobTitle() + " has been approved");
		notification.setRoute("/posted-jobs/" + job.getId());
		notificationService.sendNotification(notification);
	}

	@Override
	@Transactional
	public void rejectJob(Long id) throws HiringWireException {
		Job job = IJobRepository.findById(id)
				.orElseThrow(() -> new HiringWireException("JOB_NOT_FOUND"));

		job.setJobStatus(JobStatus.REJECTED);
		IJobRepository.save(job);

		NotificationDTO notification = new NotificationDTO();
		notification.setUserId(job.getPostedBy());
		notification.setAction("Job Rejected");
		notification.setMessage("Your job posting for " + job.getJobTitle() + " has been rejected");
		notification.setRoute("/posted-jobs/" + job.getId());
		notificationService.sendNotification(notification);
	}
}
