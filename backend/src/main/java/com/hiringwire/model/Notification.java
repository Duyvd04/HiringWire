package com.hiringwire.model;

import java.time.LocalDateTime;

import com.hiringwire.model.enums.NotificationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notifications")
public class Notification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	private String message;
	private String action;
	private String route;
	private NotificationStatus status;
	private LocalDateTime timestamp;

//	public NotificationDTO toDTO() {
//		return new NotificationDTO(
//				this.id,
//				this.user != null ? this.user.getId() : null,
//				this.message,
//				this.action,
//				this.route,
//				this.status,
//				this.timestamp
//		);
//	}
}