package com.hiringwire.dto;

import java.time.LocalDateTime;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Certification {
	private String name;
    private String issuer;
    private LocalDateTime issueDate;
    private String certificateId;
}
