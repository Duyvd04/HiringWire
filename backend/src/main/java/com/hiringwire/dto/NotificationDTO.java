//package com.hiringwire.dto;
//
//import java.time.LocalDateTime;
//
//import com.hiringwire.model.Notification;
//import com.hiringwire.model.enums.NotificationStatus;
//import com.hiringwire.model.User;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class NotificationDTO {
//	private Long id;
//	private Long userId;
//	private String message;
//	private String action;
//	private String route;
//	private NotificationStatus status;
//	private LocalDateTime timestamp;
//
//	public Notification toEntity(User user) {
//		return new Notification(this.id, user, this.message, this.action, this.route, this.status, this.timestamp);
//	}
//}