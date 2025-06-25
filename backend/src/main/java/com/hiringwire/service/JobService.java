package com.hiringwire.service;

import com.hiringwire.exception.HiringWireException;
import com.hiringwire.model.ApplicationStatus;
import com.hiringwire.model.Job;
import com.hiringwire.model.request.ApplicantRequest;
import com.hiringwire.model.request.JobRequest;
import com.hiringwire.model.response.JobResponse;
import com.hiringwire.dto.Application;

import java.util.List;

public interface JobService {

    JobResponse postJob(JobRequest jobRequest) throws HiringWireException;

    List<JobResponse> getAllJobs() throws HiringWireException;

    JobResponse getJob(Long id) throws HiringWireException;

    void applyJob(Long id, ApplicantRequest applicantRequest) throws HiringWireException;

    List<JobResponse> getHistory(Long id, ApplicationStatus applicationStatus);

    List<JobResponse> getJobsPostedBy(Long id) throws HiringWireException;

    void changeAppStatus(Application application) throws HiringWireException;

    void deleteJob(Long id) throws HiringWireException;

    Job getJobWithApplicant(Long jobId, Long applicantId) throws HiringWireException;

    List<JobResponse> getPendingJobs() throws HiringWireException;

    void approveJob(Long id) throws HiringWireException;

    void rejectJob(Long id) throws HiringWireException;
}
