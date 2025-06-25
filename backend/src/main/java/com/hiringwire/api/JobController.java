package com.hiringwire.api;

import com.hiringwire.dto.Application;
import com.hiringwire.exception.HiringWireException;
import com.hiringwire.model.ApplicationStatus;
import com.hiringwire.model.request.ApplicantRequest;
import com.hiringwire.model.request.JobRequest;
import com.hiringwire.model.response.JobResponse;
import com.hiringwire.dto.ResponseDTO;
import com.hiringwire.service.JobService;

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
public class JobController {

	private final JobService jobService;

	@PostMapping("/post")
	public ResponseEntity<JobResponse> postJob(@RequestBody @Valid JobRequest jobRequest) throws HiringWireException {
		return new ResponseEntity<>(jobService.postJob(jobRequest), HttpStatus.CREATED);
	}

	@PostMapping("/postAll")
	public ResponseEntity<List<JobResponse>> postAllJobs(@RequestBody @Valid List<JobRequest> jobRequests) {
		List<JobResponse> responses = jobRequests.stream()
				.map(request -> {
					try {
						return jobService.postJob(request);
					} catch (HiringWireException e) {
						e.printStackTrace();
						return null; // or log/skip/collect errors
					}
				})
				.filter(x -> x != null)
				.toList();
		return new ResponseEntity<>(responses, HttpStatus.CREATED);
	}

	@GetMapping("/getAll")
	public ResponseEntity<List<JobResponse>> getAllJobs() throws HiringWireException {
		return new ResponseEntity<>(jobService.getAllJobs(), HttpStatus.OK);
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<JobResponse> getJob(@PathVariable Long id) throws HiringWireException {
		return new ResponseEntity<>(jobService.getJob(id), HttpStatus.OK);
	}

	@PostMapping("/apply/{id}")
	public ResponseEntity<ResponseDTO> applyJob(@PathVariable Long id,
												@RequestBody @Valid ApplicantRequest applicantRequest) throws HiringWireException {
		jobService.applyJob(id, applicantRequest);
		return new ResponseEntity<>(new ResponseDTO("Applied Successfully"), HttpStatus.OK);
	}

	@GetMapping("/postedBy/{id}")
	public ResponseEntity<List<JobResponse>> getJobsPostedBy(@PathVariable Long id) throws HiringWireException {
		return new ResponseEntity<>(jobService.getJobsPostedBy(id), HttpStatus.OK);
	}

	@GetMapping("/history/{id}/{applicationStatus}")
	public ResponseEntity<List<JobResponse>> getHistory(@PathVariable Long id,
														@PathVariable ApplicationStatus applicationStatus) {
		return new ResponseEntity<>(jobService.getHistory(id, applicationStatus), HttpStatus.OK);
	}

	@PostMapping("/changeAppStatus")
	public ResponseEntity<ResponseDTO> changeAppStatus(@RequestBody Application application) throws HiringWireException {
		jobService.changeAppStatus(application);
		return new ResponseEntity<>(new ResponseDTO("Status Changed Successfully"), HttpStatus.OK);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<ResponseDTO> deleteJob(@PathVariable Long id) throws HiringWireException {
		jobService.deleteJob(id);
		return new ResponseEntity<>(new ResponseDTO("Deleted Successfully"), HttpStatus.OK);
	}
}
