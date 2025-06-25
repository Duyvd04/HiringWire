package com.hiringwire.repository;

import java.util.List;

import com.hiringwire.model.enums.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hiringwire.model.ApplicationStatus;
import com.hiringwire.model.Job;

public interface JobRepository extends JpaRepository<Job, Long> {
	@Query("SELECT j FROM Job j JOIN j.applicants a WHERE a.user.id = :applicantId AND a.applicationStatus = :status")
	List<Job> findByApplicantIdAndApplicationStatus(@Param("applicantId") Long applicantId,
													@Param("status") ApplicationStatus applicationStatus);

	List<Job> findByPostedBy(Long postedBy);
	List<Job> findByJobStatus(JobStatus jobStatus);
}