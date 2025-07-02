package com.hiringwire.service;

import com.hiringwire.exception.HiringWireException;
import com.hiringwire.mapper.NotificationMapper;
import com.hiringwire.model.Notification;
import com.hiringwire.model.User;
import com.hiringwire.model.enums.NotificationStatus;
import com.hiringwire.model.request.NotificationRequest;
import com.hiringwire.model.response.NotificationResponse;
import com.hiringwire.repository.NotificationRepository;
import com.hiringwire.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationMapper notificationMapper;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private User testUser;
    private Notification testNotification;
    private NotificationRequest testRequest;
    private NotificationResponse testResponse;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);

        testRequest = new NotificationRequest();
        testRequest.setUserId(1L);

        testNotification = new Notification();
        testNotification.setId(1L);
        testNotification.setUser(testUser);
        testNotification.setStatus(NotificationStatus.UNREAD);

        testResponse = new NotificationResponse();
    }

    @Test
    void sendNotification_Success() throws HiringWireException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(notificationMapper.toEntity(any(), any())).thenReturn(testNotification);

        notificationService.sendNotification(testRequest);

        verify(userRepository).findById(1L);
        verify(notificationMapper).toEntity(any(), eq(testUser));
        verify(notificationRepository).save(any(Notification.class));

        assertEquals(NotificationStatus.UNREAD, testRequest.getStatus());
        assertNotNull(testRequest.getTimestamp());
    }

    @Test
    void sendNotification_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(HiringWireException.class, () ->
                notificationService.sendNotification(testRequest));

        verify(notificationRepository, never()).save(any());
    }

    @Test
    void getUnreadNotifications_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(notificationRepository.findByUserAndStatus(testUser, NotificationStatus.UNREAD))
                .thenReturn(List.of(testNotification));
        when(notificationMapper.toResponse(testNotification)).thenReturn(testResponse);

        List<NotificationResponse> result = notificationService.getUnreadNotifications(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(notificationRepository).findByUserAndStatus(testUser, NotificationStatus.UNREAD);
    }

    @Test
    void getUnreadNotifications_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                notificationService.getUnreadNotifications(1L));

        verify(notificationRepository, never()).findByUserAndStatus(any(), any());
    }

    @Test
    void readNotification_Success() throws HiringWireException {
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(testNotification));

        notificationService.readNotification(1L);

        verify(notificationRepository).save(testNotification);
        assertEquals(NotificationStatus.READ, testNotification.getStatus());
    }

    @Test
    void readNotification_NotFound() {
        when(notificationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(HiringWireException.class, () ->
                notificationService.readNotification(1L));

        verify(notificationRepository, never()).save(any());
    }
}