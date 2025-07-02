package com.hiringwire.api;

import com.hiringwire.dto.*;
import com.hiringwire.mapper.UserMapper;
import com.hiringwire.model.User;
import com.hiringwire.exception.HiringWireException;
import com.hiringwire.model.response.JobResponse;
import com.hiringwire.model.response.UserResponse;
import com.hiringwire.service.AdminService;
import com.hiringwire.service.JobService;
import com.hiringwire.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final JobService jobService;
    private final AdminService adminService;
    private final UserMapper userMapper;


    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() throws HiringWireException {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<JobResponse>> getAllJobs() {
        try {
            List<JobResponse> jobs = jobService.getAllJobs();
            return ResponseEntity.ok(jobs);
        } catch (HiringWireException e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @GetMapping("/employers/pending")
    public ResponseEntity<List<UserResponse>> getPendingEmployers() {
        List<UserResponse> pendingEmployers = adminService.getPendingEmployers()
                .stream()
                .map(userMapper::toResponse)
                .toList();

        return ResponseEntity.ok(pendingEmployers);
    }


    @PostMapping("/employers/{id}/approve")
    public ResponseEntity<String> approveEmployer(@PathVariable Long id) throws HiringWireException {
        adminService.approveEmployer(id);
        return ResponseEntity.ok("Employer approved successfully");
    }

    @PostMapping("/employers/{id}/reject")
    public ResponseEntity<String> rejectEmployer(@PathVariable Long id) throws HiringWireException {
        adminService.rejectEmployer(id);
        return ResponseEntity.ok("Employer rejected successfully");
    }

    @PostMapping("/users/{id}/status/{AccountStatus}")
    public ResponseEntity<String> changeAccountStatus(
            @PathVariable Long id,
            @PathVariable String AccountStatus) throws HiringWireException {
        userService.changeAccountStatus(id, AccountStatus);
        return ResponseEntity.ok("Account status changed successfully");
    }
    @GetMapping("/jobs/pending")
    public ResponseEntity<List<JobResponse>> getPendingJobs() {
        try {
            List<JobResponse> pendingJobs = jobService.getPendingJobs();
            return ResponseEntity.ok(pendingJobs);
        } catch (HiringWireException e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @PostMapping("/jobs/{id}/approve")
    public ResponseEntity<String> approveJob(@PathVariable Long id) throws HiringWireException {
        jobService.approveJob(id);
        return ResponseEntity.ok("Job approved successfully");
    }

    @PostMapping("/jobs/{id}/reject")
    public ResponseEntity<String> rejectJob(@PathVariable Long id) throws HiringWireException {
        jobService.rejectJob(id);
        return ResponseEntity.ok("Job rejected successfully");
    }
}