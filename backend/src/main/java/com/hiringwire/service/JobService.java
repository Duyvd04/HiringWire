package com.hiringwire.service;

import java.util.List;

import com.hiringwire.dto.ApplicantDTO;
import com.hiringwire.dto.Application;
import com.hiringwire.dto.ApplicationStatus;
import com.hiringwire.dto.JobDTO;
import com.hiringwire.exception.HiringWireException;



public interface JobService {

	public JobDTO postJob(JobDTO jobDTO) throws HiringWireException;

	public List<JobDTO> getAllJobs() throws HiringWireException;

	public JobDTO getJob(Long id) throws HiringWireException;

	public void applyJob(Long id, ApplicantDTO applicantDTO) throws HiringWireException;

	public List<JobDTO> getHistory(Long id, ApplicationStatus applicationStatus);

	public List<JobDTO> getJobsPostedBy(Long id) throws HiringWireException;

	public void changeAppStatus(Application application) throws HiringWireException;
	
	

}
