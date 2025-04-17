package com.hiringwire.dto;

import java.time.LocalDateTime;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Application {
	private Long id;
	private Long applicantId;
	private LocalDateTime interviewTime;
	private ApplicationStatus applicationStatus;
}
