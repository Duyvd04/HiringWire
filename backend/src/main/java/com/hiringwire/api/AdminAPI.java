package com.hiringwire.api;

import com.hiringwire.dto.JobDTO;
import com.hiringwire.dto.UserDTO;
import com.hiringwire.exception.HiringWireException;
import com.hiringwire.service.JobService;
import com.hiringwire.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminAPI {
    @Autowired
    private UserService userService;

    @Autowired
    private JobService jobService;

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
    @PostMapping("changeAccountStatus/{id}/{AccountStatus}")
    public ResponseEntity<String> changeAccountStatus(@PathVariable Long id, @PathVariable String AccountStatus) throws HiringWireException {
        userService.changeAccountStatus(id, AccountStatus);
        return ResponseEntity.ok("Account status changed successfully");
    }
}
