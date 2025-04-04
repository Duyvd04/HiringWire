package com.hiringwire.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hiringwire.dto.NotificationStatus;
import com.hiringwire.entity.Notification;

public interface INotificationRepository extends JpaRepository<Notification, Long> {
	List<Notification> findByUserIdAndStatus(Long userId, NotificationStatus status);
}