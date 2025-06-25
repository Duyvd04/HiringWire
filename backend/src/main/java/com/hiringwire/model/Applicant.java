package com.hiringwire.model;

import java.time.LocalDateTime;
import java.util.Base64;

import com.hiringwire.dto.ApplicantDTO;

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

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "applicant_id", nullable = false)
	private User user; // Replaced applicantId, name, and email with User reference

	private String phone;
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

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "job_id")
	private Job job;

//	public ApplicantDTO toDTO() {
//		return new ApplicantDTO(
//				this.getId(),
//				this.getUser().getId(), // applicantId from User
//				this.getUser().getName(), // name from User
//				this.getUser().getEmail(), // email from User
//				this.getPhone(),
//				this.getWebsite(),
//				this.getResume() != null ? Base64.getEncoder().encodeToString(this.getResume()) : null,
//				this.getExtractedResume() != null ? Base64.getEncoder().encodeToString(this.getExtractedResume()) : null,
//				this.getCoverLetter(),
//				this.getTimestamp(),
//				this.getApplicationStatus(),
//				this.getInterviewTime()
//		);
//	}
}