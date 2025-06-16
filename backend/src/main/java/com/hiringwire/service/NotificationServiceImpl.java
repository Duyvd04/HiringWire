package com.hiringwire.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hiringwire.dto.NotificationDTO;
import com.hiringwire.dto.NotificationStatus;
import com.hiringwire.entity.Notification;
import com.hiringwire.entity.User;
import com.hiringwire.exception.HiringWireException;
import com.hiringwire.repository.INotificationRepository;
import com.hiringwire.repository.IUserRepository;

@Service("notificationService")
public class NotificationServiceImpl implements NotificationService {
	@Autowired
	private INotificationRepository INotificationRepository;

	@Autowired
	private IUserRepository userRepository;

	@Override
	public void sendNotification(NotificationDTO notificationDTO) throws HiringWireException {
		User user = userRepository.findById(notificationDTO.getUserId())
				.orElseThrow(() -> new HiringWireException("User not found"));
		notificationDTO.setStatus(NotificationStatus.UNREAD);
		notificationDTO.setTimestamp(LocalDateTime.now());
		Notification savedNotification = INotificationRepository.save(notificationDTO.toEntity(user));
	}

	@Override
	public List<Notification> getUnreadNotifications(Long userId) {
        User user = null;
        try {
            user = userRepository.findById(userId)
                    .orElseThrow(() -> new HiringWireException("User not found"));
        } catch (HiringWireException e) {
            throw new RuntimeException(e);
        }
        return INotificationRepository.findByUserAndStatus(user, NotificationStatus.UNREAD);
	}

	@Override
	public void readNotification(Long id) throws HiringWireException {
		Notification noti = INotificationRepository.findById(id)
				.orElseThrow(() -> new HiringWireException("No Notification found"));
		noti.setStatus(NotificationStatus.READ);
		INotificationRepository.save(noti);
	}
}