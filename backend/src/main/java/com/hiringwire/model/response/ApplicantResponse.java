package com.hiringwire.model.response;

import com.hiringwire.model.ApplicationStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApplicantResponse {
    private Long id;
    private Long applicantId; // from User
    private String name;      // from User
    private String email;     // from User
    private String phone;
    private String website;
    private String resume; // Base64 string
    private String extractedResume; // Base64 string
    private String coverLetter;
    private LocalDateTime timestamp;
    private ApplicationStatus applicationStatus;
    private LocalDateTime interviewTime;
}
