package com.hiringwire.entity;

import java.time.LocalDateTime;
import java.util.Base64;

import com.hiringwire.dto.ApplicantDTO;
import com.hiringwire.dto.ApplicationStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Applicant {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long applicantId;
	private String name;
	private String email;
	private Long phone;
	private String website;
	private byte[] resume;
	private String coverLetter;
	private LocalDateTime timestamp;
	private ApplicationStatus applicationStatus;
	private LocalDateTime interviewTime;

	public ApplicantDTO toDTO() {
		return new ApplicantDTO(
				this.getApplicantId(),
				this.getName(),
				this.getEmail(),
				this.getPhone(),
				this.getWebsite(),
				this.getResume() != null ? Base64.getEncoder().encodeToString(this.getResume()) : null,
				this.getCoverLetter(),
				this.getTimestamp(),
				this.getApplicationStatus(),
				this.interviewTime
		);
	}
}