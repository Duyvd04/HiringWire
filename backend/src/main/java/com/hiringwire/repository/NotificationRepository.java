package com.hiringwire.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hiringwire.model.enums.NotificationStatus;
import com.hiringwire.model.Notification;
import com.hiringwire.model.User;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
	List<Notification> findByUserAndStatus(User user, NotificationStatus status);
}