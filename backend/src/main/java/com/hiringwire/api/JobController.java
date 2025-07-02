package com.hiringwire.api;

import com.hiringwire.dto.Application;
import com.hiringwire.exception.HiringWireException;
import com.hiringwire.model.ApplicationStatus;
import com.hiringwire.model.request.ApplicantRequest;
import com.hiringwire.model.request.JobRequest;
import com.hiringwire.model.response.JobResponse;
import com.hiringwire.dto.ResponseDTO;
import com.hiringwire.service.JobService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/jobs")
@Validated
@RequiredArgsConstructor
@Tag(name = "Job Controller", description = "APIs for job postings, applications, and job management")
public class JobController {

	private final JobService jobService;

	@Operation(summary = "Post a new job", description = "Create a new job posting")
	@PostMapping("/post")
	public ResponseEntity<JobResponse> postJob(
			@RequestBody @Valid JobRequest jobRequest) throws HiringWireException {
		return new ResponseEntity<>(jobService.postJob(jobRequest), HttpStatus.CREATED);
	}

	@Operation(summary = "Post multiple jobs", description = "Create multiple job postings at once")
	@PostMapping("/postAll")
	public ResponseEntity<List<JobResponse>> postAllJobs(
			@RequestBody @Valid List<JobRequest> jobRequests) {
		List<JobResponse> responses = jobRequests.stream()
				.map(request -> {
					try {
						return jobService.postJob(request);
					} catch (HiringWireException e) {
						e.printStackTrace();
						return null;
					}
				})
				.filter(x -> x != null)
				.toList();
		return new ResponseEntity<>(responses, HttpStatus.CREATED);
	}

	@Operation(summary = "Get all jobs", description = "Fetch list of all posted jobs")
	@GetMapping("/getAll")
	public ResponseEntity<List<JobResponse>> getAllJobs() throws HiringWireException {
		return new ResponseEntity<>(jobService.getAllJobs(), HttpStatus.OK);
	}

	@Operation(summary = "Get a job by ID", description = "Fetch a specific job by its ID")
	@GetMapping("/get/{id}")
	public ResponseEntity<JobResponse> getJob(
			@Parameter(description = "ID of the job to retrieve") @PathVariable Long id) throws HiringWireException {
		return new ResponseEntity<>(jobService.getJob(id), HttpStatus.OK);
	}

	@Operation(summary = "Apply for a job", description = "Submit application to a specific job")
	@PostMapping("/apply/{id}")
	public ResponseEntity<ResponseDTO> applyJob(
			@Parameter(description = "ID of the job to apply") @PathVariable Long id,
			@RequestBody @Valid ApplicantRequest applicantRequest) throws HiringWireException {
		jobService.applyJob(id, applicantRequest);
		return new ResponseEntity<>(new ResponseDTO("Applied Successfully"), HttpStatus.OK);
	}

	@Operation(summary = "Get jobs posted by employer", description = "Fetch jobs posted by a specific user")
	@GetMapping("/postedBy/{id}")
	public ResponseEntity<List<JobResponse>> getJobsPostedBy(
			@Parameter(description = "User ID") @PathVariable Long id) throws HiringWireException {
		return new ResponseEntity<>(jobService.getJobsPostedBy(id), HttpStatus.OK);
	}

	@Operation(summary = "Get job application history by user and status", description = "Retrieve job application history with a specific status")
	@GetMapping("/history/{id}/{applicationStatus}")
	public ResponseEntity<List<JobResponse>> getHistory(
			@Parameter(description = "User ID") @PathVariable Long id,
			@Parameter(description = "Application Status") @PathVariable ApplicationStatus applicationStatus) {
		return new ResponseEntity<>(jobService.getHistory(id, applicationStatus), HttpStatus.OK);
	}

	@Operation(summary = "Change application status", description = "Update the status of a specific job application")
	@PostMapping("/changeAppStatus")
	public ResponseEntity<ResponseDTO> changeAppStatus(
			@RequestBody Application application) throws HiringWireException {
		jobService.changeAppStatus(application);
		return new ResponseEntity<>(new ResponseDTO("Status Changed Successfully"), HttpStatus.OK);
	}

	@Operation(summary = "Delete a job", description = "Delete a job by its ID")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<ResponseDTO> deleteJob(
			@Parameter(description = "ID of the job to delete") @PathVariable Long id) throws HiringWireException {
		jobService.deleteJob(id);
		return new ResponseEntity<>(new ResponseDTO("Deleted Successfully"), HttpStatus.OK);
	}
}
