package com.hiringwire.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hiringwire.dto.NotificationDTO;
import com.hiringwire.dto.NotificationStatus;
import com.hiringwire.entity.Notification;
import com.hiringwire.exception.HiringWireException;
import com.hiringwire.repository.NotificationRepository;
import com.hiringwire.utility.Utilities;

@Service("notificationService")
public class NotificationServiceImpl implements NotificationService {
	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private Utilities utilities; // Add this

	@Override
	public void sendNotification(NotificationDTO notificationDTO) throws HiringWireException {
		notificationDTO.setStatus(NotificationStatus.UNREAD);
		notificationDTO.setTimestamp(LocalDateTime.now());
		Notification savedNotification = notificationRepository.save(notificationDTO.toEntity());
		// ID is set by JPA, no need to set it manually
	}

	@Override
	public List<Notification> getUnreadNotifications(Long userId) {
		return notificationRepository.findByUserIdAndStatus(userId, NotificationStatus.UNREAD);
	}

	@Override
	public void readNotification(Long id) throws HiringWireException {
		Notification noti = notificationRepository.findById(id)
				.orElseThrow(() -> new HiringWireException("No Notification found"));
		noti.setStatus(NotificationStatus.READ);
		notificationRepository.save(noti);
	}
}