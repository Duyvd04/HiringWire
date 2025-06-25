package com.hiringwire.service;

import java.time.LocalDateTime;
import java.util.List;

import com.hiringwire.mapper.NotificationMapper;
import com.hiringwire.model.request.NotificationRequest;
import com.hiringwire.model.response.NotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.hiringwire.model.enums.NotificationStatus;
import com.hiringwire.model.Notification;
import com.hiringwire.model.User;
import com.hiringwire.exception.HiringWireException;
import com.hiringwire.repository.NotificationRepository;
import com.hiringwire.repository.UserRepository;

@Service("notificationService")
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
	private final NotificationRepository INotificationRepository;
	private final UserRepository userRepository;
	private final NotificationMapper notificationMapper;

	@Override
	public void sendNotification(NotificationRequest notificationRequest) throws HiringWireException {
		User user = userRepository.findById(notificationRequest.getUserId())
				.orElseThrow(() -> new HiringWireException("User not found"));

		notificationRequest.setStatus(NotificationStatus.UNREAD);
		notificationRequest.setTimestamp(LocalDateTime.now());

		Notification notification = notificationMapper.toEntity(notificationRequest, user);
		INotificationRepository.save(notification);
	}

	@Override
	public List<NotificationResponse> getUnreadNotifications(Long userId) {
		try {
			User user = userRepository.findById(userId)
					.orElseThrow(() -> new HiringWireException("User not found"));
			return INotificationRepository.findByUserAndStatus(user, NotificationStatus.UNREAD)
					.stream()
					.map(notificationMapper::toResponse)
					.toList();
		} catch (HiringWireException e) {
			throw new RuntimeException(e);
		}
	}
	@Override
	public void readNotification(Long id) throws HiringWireException {
		Notification notification = INotificationRepository.findById(id)
				.orElseThrow(() -> new HiringWireException("No Notification found"));
		notification.setStatus(NotificationStatus.READ);
		INotificationRepository.save(notification);
	}
}