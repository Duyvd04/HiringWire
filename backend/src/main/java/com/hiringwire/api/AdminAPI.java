package com.hiringwire.api;

import com.hiringwire.dto.*;
import com.hiringwire.entity.Job;
import com.hiringwire.exception.HiringWireException;
import com.hiringwire.service.AdminService;
import com.hiringwire.service.JobService;
import com.hiringwire.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminAPI {
    @Autowired
    private UserService userService;

    @Autowired
    private JobService jobService;

    @Autowired
    private AdminService adminService;


    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() throws HiringWireException {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<JobDTO>> getAllJobs() {
        try {
            List<JobDTO> jobs = jobService.getAllJobs();
            return ResponseEntity.ok(jobs);
        } catch (HiringWireException e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @GetMapping("/employers/pending")
    public ResponseEntity<List<UserDTO>> getPendingEmployers() {
        List<UserDTO> pendingEmployers = adminService.getPendingEmployers()
                .stream()
                .map(user -> user.toDTO())
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
    public ResponseEntity<List<JobDTO>> getPendingJobs() {
        try {
            List<JobDTO> pendingJobs = jobService.getPendingJobs();
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