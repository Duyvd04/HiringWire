package com.hiringwire.service;

import com.hiringwire.dto.*;
import com.hiringwire.model.enums.AccountStatus;
import com.hiringwire.model.enums.AccountType;
import com.hiringwire.model.User;
import com.hiringwire.exception.HiringWireException;
import com.hiringwire.model.request.NotificationRequest;
import com.hiringwire.model.response.NotificationResponse;
import com.hiringwire.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Override
    public List<User> getPendingEmployers() {
        return userRepository.findAll().stream()
                .filter(user -> user.getAccountType() == AccountType.EMPLOYER
                        && user.getAccountStatus() == AccountStatus.PENDING_APPROVAL)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void approveEmployer(Long id) throws HiringWireException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new HiringWireException("USER_NOT_FOUND"));

        if (user.getAccountType() != AccountType.EMPLOYER) {
            throw new HiringWireException("USER_NOT_EMPLOYER");
        }

        user.setAccountStatus(AccountStatus.ACTIVE);
        userRepository.save(user);

        // Send notification
        notificationService.sendNotification(createNotification(user.getId(),
                "Account Approved",
                "Your employer account has been approved. You can now login and post jobs."));
    }

    @Override
    @Transactional
    public void rejectEmployer(Long id) throws HiringWireException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new HiringWireException("USER_NOT_FOUND"));

        if (user.getAccountType() != AccountType.EMPLOYER) {
            throw new HiringWireException("USER_NOT_EMPLOYER");
        }

        user.setAccountStatus(AccountStatus.REJECTED);
        userRepository.save(user);

        // Send notification
        notificationService.sendNotification(createNotification(user.getId(),
                "Account Rejected",
                "Your employer account has been rejected. Please contact support for more information."));
    }


    private NotificationRequest createNotification(Long userId, String action, String message) {
        NotificationRequest notification = new NotificationRequest();
        notification.setUserId(userId);
        notification.setAction(action);
        notification.setMessage(message);
        return notification;
    }
}