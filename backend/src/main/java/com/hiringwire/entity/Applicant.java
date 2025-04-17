package com.hiringwire.entity;

import java.time.LocalDateTime;
import java.util.Base64;

import com.hiringwire.dto.ApplicantDTO;
import com.hiringwire.dto.ApplicationStatus;

import jakarta.persistence.*;
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
	private Long id;
	private Long applicantId;  // This is the user ID
	private String name;
	private String email;
	private Long phone;
	private String website;
	@Lob
	@Column(name = "resume", columnDefinition = "LONGBLOB")
	private byte[] resume;
	@Lob
	@Column(name = "extracted_resume", columnDefinition = "LONGBLOB")
	private byte[] extractedResume;
	private String coverLetter;
	private LocalDateTime timestamp;
	private ApplicationStatus applicationStatus;
	private LocalDateTime interviewTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "job_id")
	private Job job;

	public ApplicantDTO toDTO() {
		return new ApplicantDTO(
				this.getId(),
				this.getApplicantId(),
				this.getName(),
				this.getEmail(),
				this.getPhone(),
				this.getWebsite(),
				this.getResume() != null ? Base64.getEncoder().encodeToString(this.getResume()) : null,
				this.getExtractedResume() != null ? Base64.getEncoder().encodeToString(this.getExtractedResume()) : null,
				this.getCoverLetter(),
				this.getTimestamp(),
				this.getApplicationStatus(),
				this.interviewTime
		);
	}

}