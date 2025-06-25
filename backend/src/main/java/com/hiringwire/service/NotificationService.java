package com.hiringwire.service;

import java.util.List;

import com.hiringwire.model.Notification;
import com.hiringwire.exception.HiringWireException;
import com.hiringwire.model.request.NotificationRequest;
import com.hiringwire.model.response.NotificationResponse;

public interface NotificationService {
	void sendNotification(NotificationRequest notificationRequest) throws HiringWireException;
	List<NotificationResponse> getUnreadNotifications(Long userId);
	void readNotification(Long id) throws HiringWireException;
}