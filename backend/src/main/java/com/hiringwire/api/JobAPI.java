package com.hiringwire.api;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hiringwire.entity.Applicant;
import com.hiringwire.entity.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.hiringwire.dto.ApplicantDTO;
import com.hiringwire.dto.Application;
import com.hiringwire.dto.ApplicationStatus;
import com.hiringwire.dto.JobDTO;
import com.hiringwire.dto.ResponseDTO;
import com.hiringwire.exception.HiringWireException;
import com.hiringwire.service.JobService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/jobs")
@Validated
public class JobAPI {
	
	@Autowired 
	private JobService jobService;

	@PostMapping("/post")
	public ResponseEntity<JobDTO>postJob(@RequestBody @Valid JobDTO jobDTO) throws HiringWireException {
		return new ResponseEntity<>(jobService.postJob(jobDTO), HttpStatus.CREATED);
	}
	
	
	@PostMapping("/postAll")
	public ResponseEntity<List<JobDTO>>postAllJob(@RequestBody @Valid List<JobDTO> jobDTOs) throws HiringWireException {
		
		return new ResponseEntity<>(jobDTOs.stream().map((x)->{
			try {
				return jobService.postJob(x);
			} catch (HiringWireException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return x;
		}).toList() , HttpStatus.CREATED);
	}
	
	@GetMapping("/getAll")
	public ResponseEntity<List<JobDTO>>getAllJobs() throws HiringWireException {
		return new ResponseEntity<>(jobService.getAllJobs(), HttpStatus.OK);
	}
	@GetMapping("/get/{id}")
	public ResponseEntity<JobDTO>getJob(@PathVariable Long id) throws HiringWireException {
		return new ResponseEntity<>(jobService.getJob(id), HttpStatus.OK);
	}
	@PostMapping("apply/{id}")
	public ResponseEntity<ResponseDTO>applyJob(@PathVariable Long id, @RequestBody ApplicantDTO applicantDTO) throws HiringWireException {
		jobService.applyJob(id, applicantDTO);
		return new ResponseEntity<>(new ResponseDTO("Applied Successfully"), HttpStatus.OK);
	}
	@GetMapping("/postedBy/{id}")
	public ResponseEntity<List<JobDTO>>getJobsPostedBy(@PathVariable Long id) throws HiringWireException {
		return new ResponseEntity<>(jobService.getJobsPostedBy(id), HttpStatus.OK);
	}
	
	@GetMapping("/history/{id}/{applicationStatus}")
	public ResponseEntity<List<JobDTO>>getHistory(@PathVariable Long id,@PathVariable ApplicationStatus applicationStatus) throws HiringWireException {
		return new ResponseEntity<>(jobService.getHistory(id, applicationStatus), HttpStatus.OK);
	}
	@PostMapping("/changeAppStatus")
	public ResponseEntity<ResponseDTO>changeAppStatus(@RequestBody Application application) throws HiringWireException {
		jobService.changeAppStatus(application);
		return new ResponseEntity<>(new ResponseDTO("Status Changed Successfully"), HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<ResponseDTO>deleteJob(@PathVariable Long id) throws HiringWireException{
		jobService.deleteJob(id);
		return new ResponseEntity<>(new ResponseDTO("Delete Successfully"),HttpStatus.OK);
	}
}
