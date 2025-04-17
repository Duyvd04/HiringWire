package com.hiringwire.service;

import java.util.List;

import com.hiringwire.dto.NotificationDTO;
import com.hiringwire.entity.Notification;
import com.hiringwire.exception.HiringWireException;

public interface NotificationService {
	void sendNotification(NotificationDTO notificationDTO) throws HiringWireException;
	List<Notification> getUnreadNotifications(Long userId);
	void readNotification(Long id) throws HiringWireException;
}