package com.hiringwire.dto;

import java.time.LocalDateTime;
import java.util.Base64;

import com.hiringwire.entity.Applicant;
import com.hiringwire.entity.User;
import com.hiringwire.dto.ApplicationStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicantDTO {
	private Long id;
	private Long applicantId;
	private String name;
	private String email;
	private Long phone;
	private String website;
	private String resume;
	private String extractedResume;
	private String coverLetter;
	private LocalDateTime timestamp;
	private ApplicationStatus applicationStatus;
	private LocalDateTime interviewTime;

	public Applicant toEntity() {
		Applicant applicant = new Applicant();
		applicant.setId(this.getId());
		// User entity will be set by the service layer, not here
		applicant.setPhone(this.getPhone());
		applicant.setWebsite(this.getWebsite());
		applicant.setResume(this.getResume() != null ? Base64.getDecoder().decode(this.getResume()) : null);
		applicant.setExtractedResume(this.getExtractedResume() != null ? Base64.getDecoder().decode(this.getExtractedResume()) : null);
		applicant.setCoverLetter(this.getCoverLetter());
		applicant.setTimestamp(this.getTimestamp());
		applicant.setApplicationStatus(this.getApplicationStatus());
		applicant.setInterviewTime(this.getInterviewTime());
		return applicant;
	}
}