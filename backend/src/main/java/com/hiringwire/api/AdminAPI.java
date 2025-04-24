package com.hiringwire.api;

import com.hiringwire.dto.*;
import com.hiringwire.exception.HiringWireException;
import com.hiringwire.service.AdminService;
import com.hiringwire.service.JobService;
import com.hiringwire.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<JobDTO>> getAllJobs() throws HiringWireException {
        List<JobDTO> jobs = jobService.getAllJobs();
        return ResponseEntity.ok(jobs);
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

}