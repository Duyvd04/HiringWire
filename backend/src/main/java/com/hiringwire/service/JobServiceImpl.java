package com.hiringwire.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.hiringwire.repository.JobRepository;
import com.hiringwire.utility.Utilities;

@Service("jobService")
public class JobServiceImpl implements JobService {

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private Utilities utilities; // Add this

	@Override
	public JobDTO postJob(JobDTO jobDTO) throws HiringWireException {
		if (jobDTO.getId() == null || jobDTO.getId() == 0) { // Check for null or 0 since ID is auto-generated
			jobDTO.setPostTime(LocalDateTime.now());
			NotificationDTO notiDto = new NotificationDTO();
			notiDto.setAction("Job Posted");
			notiDto.setMessage("Job Posted Successfully for " + jobDTO.getJobTitle() + " at " + jobDTO.getCompany());
			notiDto.setUserId(jobDTO.getPostedBy());
			// ID will be set after save, so set route later
			Job savedJob = jobRepository.save(jobDTO.toEntity());
			notiDto.setRoute("/posted-jobs/" + savedJob.getId());
			notificationService.sendNotification(notiDto);
			return savedJob.toDTO();
		} else {
			Job job = jobRepository.findById(jobDTO.getId())
					.orElseThrow(() -> new HiringWireException("JOB_NOT_FOUND"));
			if (job.getJobStatus().equals(JobStatus.DRAFT) || jobDTO.getJobStatus().equals(JobStatus.CLOSED))
				jobDTO.setPostTime(LocalDateTime.now());
			return jobRepository.save(jobDTO.toEntity()).toDTO();
		}
	}

	@Override
	public List<JobDTO> getAllJobs() throws HiringWireException {
		return jobRepository.findAll().stream().map((x) -> x.toDTO()).toList();
	}

	@Override
	public JobDTO getJob(Long id) throws HiringWireException {
		return jobRepository.findById(id).orElseThrow(() -> new HiringWireException("JOB_NOT_FOUND")).toDTO();
	}

	@Override
	public void applyJob(Long id, ApplicantDTO applicantDTO) throws HiringWireException {
		Job job = jobRepository.findById(id).orElseThrow(() -> new HiringWireException("JOB_NOT_FOUND"));
		List<Applicant> applicants = job.getApplicants();
		if (applicants == null) applicants = new ArrayList<>();
		if (applicants.stream().anyMatch((x) -> x.getApplicantId().equals(applicantDTO.getApplicantId())))
			throw new HiringWireException("JOB_APPLIED_ALREADY");
		applicantDTO.setApplicationStatus(ApplicationStatus.APPLIED);
		applicants.add(applicantDTO.toEntity());
		job.setApplicants(applicants);
		jobRepository.save(job);
	}

	@Override
	public List<JobDTO> getHistory(Long id, ApplicationStatus applicationStatus) {
		return jobRepository.findByApplicantIdAndApplicationStatus(id, applicationStatus)
				.stream().map((x) -> x.toDTO()).toList();
	}

	@Override
	public List<JobDTO> getJobsPostedBy(Long id) throws HiringWireException {
		return jobRepository.findByPostedBy(id).stream().map((x) -> x.toDTO()).toList();
	}

	@Override
	public void changeAppStatus(Application application) throws HiringWireException {
		Job job = jobRepository.findById(application.getId())
				.orElseThrow(() -> new HiringWireException("JOB_NOT_FOUND"));
		List<Applicant> apps = job.getApplicants().stream().map((x) -> {
			if (application.getApplicantId().equals(x.getApplicantId())) {
				x.setApplicationStatus(application.getApplicationStatus());
				if (application.getApplicationStatus().equals(ApplicationStatus.INTERVIEWING)) {
					x.setInterviewTime(application.getInterviewTime());
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
			}
			return x;
		}).toList();
		job.setApplicants(apps);
		jobRepository.save(job);
	}
}