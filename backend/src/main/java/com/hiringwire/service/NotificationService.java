package com.hiringwire.service;

import java.util.List;

import com.hiringwire.dto.NotificationDTO;
import com.hiringwire.entity.Notification;
import com.hiringwire.exception.HiringWireException;

public interface NotificationService {
	public void sendNotification(NotificationDTO notificationDTO) throws HiringWireException;
	public List<Notification> getUnreadNotifications(Long userId);
	public void readNotification(Long id) throws HiringWireException;
}
