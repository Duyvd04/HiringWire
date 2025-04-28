package com.hiringwire.service;

import com.hiringwire.dto.ApplicantDTO;
import com.hiringwire.dto.Application;
import com.hiringwire.dto.ApplicationStatus;
import com.hiringwire.dto.JobDTO;
import com.hiringwire.entity.Job;
import com.hiringwire.exception.HiringWireException;

import java.util.List;


public interface JobService {

    JobDTO postJob(JobDTO jobDTO) throws HiringWireException;

    List<JobDTO> getAllJobs() throws HiringWireException;

    JobDTO getJob(Long id) throws HiringWireException;

    void applyJob(Long id, ApplicantDTO applicantDTO) throws HiringWireException;

    List<JobDTO> getHistory(Long id, ApplicationStatus applicationStatus);

    List<JobDTO> getJobsPostedBy(Long id) throws HiringWireException;

    void changeAppStatus(Application application) throws HiringWireException;

	void deleteJob(Long id) throws HiringWireException;
    Job getJobWithApplicant(Long jobId, Long applicantId) throws HiringWireException;
    List<JobDTO> getPendingJobs() throws HiringWireException;
    void approveJob(Long id) throws HiringWireException;
    void rejectJob(Long id) throws HiringWireException;
}
