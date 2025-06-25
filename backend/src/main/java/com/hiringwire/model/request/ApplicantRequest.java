package com.hiringwire.model.request;

import com.hiringwire.model.ApplicationStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApplicantRequest {
    private Long id;
    private Long applicantId;
    private String name;
    private String email;
    private String phone;
    private String website;
    private String resume;
    private String extractedResume;
    private String coverLetter;
    private LocalDateTime timestamp;
    private ApplicationStatus applicationStatus;
    private LocalDateTime interviewTime;
}
