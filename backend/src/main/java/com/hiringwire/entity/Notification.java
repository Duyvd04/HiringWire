package com.hiringwire.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import com.hiringwire.dto.NotificationDTO;
import com.hiringwire.dto.NotificationStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notifications") // Changed to plural for consistency
public class Notification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long userId;
	private String message;
	private String action;
	private String route;
	private NotificationStatus status;
	private LocalDateTime timestamp;

	public NotificationDTO toDTO() {
		return new NotificationDTO(
				this.id,
				this.userId,
				this.message,
				this.action,
				this.route,
				this.status,
				this.timestamp
		);
	}
}