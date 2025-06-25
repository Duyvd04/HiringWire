package com.hiringwire.service;

import com.hiringwire.dto.*;
import com.hiringwire.mapper.UserMapper;
import com.hiringwire.model.*;
import com.hiringwire.exception.HiringWireException;
import com.hiringwire.model.enums.AccountStatus;
import com.hiringwire.model.enums.AccountType;
import com.hiringwire.model.request.LoginRequest;
import com.hiringwire.model.request.NotificationRequest;
import com.hiringwire.model.request.UserRequest;
import com.hiringwire.model.response.UserResponse;
import com.hiringwire.repository.OTPRepository;
import com.hiringwire.repository.UserRepository;
import com.hiringwire.utility.Data;
import com.hiringwire.utility.Utilities;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository IUserRepository;

    private final OTPRepository IOTPRepository;

    private final PasswordEncoder passwordEncoder;

    private final JavaMailSender mailSender;

    private final NotificationService notificationService;

    private final Utilities utilities;

    private final UserMapper userMapper;


    @Override
    public UserResponse registerUser(UserRequest userRequest) throws HiringWireException {
        if (IUserRepository.existsByEmail(userRequest.getEmail())) {
            throw new HiringWireException("User with this email already exists");
        }

        User user = userMapper.toEntity(userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        Profile profile = new Profile();
        profile.setName(userRequest.getName());
        profile.setEmail(userRequest.getEmail());
        profile.setAccountType(userRequest.getAccountType().toString());

        user.setProfile(profile);

        user.setAccountStatus(userRequest.getAccountType() == AccountType.EMPLOYER
                ? AccountStatus.PENDING_APPROVAL
                : AccountStatus.ACTIVE);

        user.setLastLoginDate(LocalDateTime.now());

        User savedUser = IUserRepository.save(user);
        return userMapper.toResponse(savedUser);
    }


    @Override
    @Transactional
    public UserResponse loginUser(LoginRequest loginRequest) throws HiringWireException {
        User user = IUserRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new HiringWireException("USER_NOT_FOUND"));

        // Check account status first
        if (user.getAccountStatus() == AccountStatus.BLOCKED) {
            throw new HiringWireException("ACCOUNT_BLOCKED");
        }

        if (user.getAccountStatus() == AccountStatus.PENDING_APPROVAL) {
            throw new HiringWireException("ACCOUNT_PENDING_APPROVAL");
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new HiringWireException("INVALID_CREDENTIALS");
        }

        user.setLastLoginDate(LocalDateTime.now());
        if (user.getAccountStatus() == AccountStatus.INACTIVE) {
            String oldStatus = user.getAccountStatus().toString();
            user.setAccountStatus(AccountStatus.ACTIVE);
            sendStatusChangeNotification(user, oldStatus, "ACTIVE",
                    "Your account has been reactivated after successful login.");
        }


        User savedUser = IUserRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    @Override
    public Boolean sendOTP(String email) throws Exception {
        User user = IUserRepository.findByEmail(email)
                .orElseThrow(() -> new HiringWireException("USER_NOT_FOUND"));

        MimeMessage mm = mailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mm, true);
        message.setTo(email);
        message.setFrom("vuducduy1112004@gmail.com", "HiringWire");
        message.setSubject("Your OTP Code");

        String generatedOtp = utilities.generateOTP();

        OTP otp = new OTP();
        otp.setUser(user);
        otp.setOtpCode(generatedOtp);
        otp.setCreationTime(LocalDateTime.now());

        IOTPRepository.save(otp);

        message.setText(Data.getMessageBody(generatedOtp, user.getName()), true);
        mailSender.send(mm);

        return true;
    }


    @Override
    public Boolean verifyOtp(String email, String otp) throws HiringWireException {
        OTP otpEntity = IOTPRepository.findByUserEmail(email)
                .orElseThrow(() -> new HiringWireException("OTP_NOT_FOUND"));

        if (!otpEntity.getOtpCode().equals(otp)) {
            throw new HiringWireException("OTP_INCORRECT");
        }

        if (otpEntity.getCreationTime().isBefore(LocalDateTime.now().minusMinutes(5))) {
            throw new HiringWireException("OTP_EXPIRED");
        }

        return true;
    }




    @Scheduled(fixedRate = 60000)
    public void removeExpiredOTPs() {
        LocalDateTime expiryTime = LocalDateTime.now().minusMinutes(5);
        List<OTP> expiredOTPs = IOTPRepository.findByCreationTimeBefore(expiryTime);
        if (!expiredOTPs.isEmpty()) {
            IOTPRepository.deleteAll(expiredOTPs);
            System.out.println("Removed " + expiredOTPs.size() + " expired OTPs");
        }
    }

    @Override
    public ResponseDTO changePassword(LoginRequest loginRequest) throws HiringWireException {
        User user = IUserRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new HiringWireException("USER_NOT_FOUND"));
        user.setPassword(passwordEncoder.encode(loginRequest.getPassword()));
        IUserRepository.save(user);
        NotificationRequest noti = new NotificationRequest();
        noti.setUserId(user.getId());
        noti.setMessage("Password Reset Successful");
        noti.setAction("Password Reset");
        notificationService.sendNotification(noti);
        return new ResponseDTO("Password changed successfully.");
    }


    @Override
    public List<UserResponse> getAllUsers() throws HiringWireException {
        List<User> users = IUserRepository.findAll();
        if (users.isEmpty())
            throw new HiringWireException("NO_USERS_FOUND");
        return users.stream().map(userMapper::toResponse).toList();
    }

    @Override
    public void changeAccountStatus(Long id, String accountStatus) throws HiringWireException {
        User user = IUserRepository.findById(id)
                .orElseThrow(() -> new HiringWireException("USER_NOT_FOUND"));

        String oldStatus = user.getAccountStatus().toString();
        String reason;

        if (accountStatus.equalsIgnoreCase("ACTIVE")) {
            user.setAccountStatus(AccountStatus.ACTIVE);
            reason = "Your account is now active.";
        } else if (accountStatus.equalsIgnoreCase("INACTIVE")) {
            user.setAccountStatus(AccountStatus.INACTIVE);
            reason = "Your account has been marked as inactive due to inactivity.";
        } else if (accountStatus.equalsIgnoreCase("BLOCKED")) {
            user.setAccountStatus(AccountStatus.BLOCKED);
            reason = "Your account has been blocked. Please contact support.";
        } else {
            throw new HiringWireException("INVALID_STATUS");
        }

        IUserRepository.save(user);
        sendStatusChangeNotification(user, oldStatus, accountStatus.toUpperCase(), reason);
    }

    @Override
    public UserResponse getUserByEmail(String email) throws HiringWireException {
        return userMapper.toResponse(IUserRepository.findByEmail(email)
                .orElseThrow(() -> new HiringWireException("USER_NOT_FOUND")));
    }

    @Scheduled(cron = "0 0 0 * * ?") // Runs daily at midnight
    public void checkInactiveUsers() throws HiringWireException {
        LocalDateTime inactiveThreshold = LocalDateTime.now().minusDays(15);
        List<User> users = IUserRepository.findByLastLoginDateBeforeAndAccountStatus(
                inactiveThreshold, AccountStatus.ACTIVE);

        for (User user : users) {
            String oldStatus = user.getAccountStatus().toString();
            user.setAccountStatus(AccountStatus.INACTIVE);
            IUserRepository.save(user);

            sendStatusChangeNotification(user, oldStatus, "INACTIVE",
                    "Your account has been marked as inactive due to 15 days of inactivity.");
        }
    }

    private void sendStatusChangeNotification(User user, String oldStatus, String newStatus, String reason) throws HiringWireException {
        // In-app notification
        NotificationRequest notification = new NotificationRequest();
        notification.setUserId(user.getId());
        notification.setAction("Account Status Change");

        String message = String.format("Your account status has been changed from %s to %s. %s",
                oldStatus, newStatus, reason);
        notification.setMessage(message);

        notificationService.sendNotification(notification);

        // Email notification
        try {
            MimeMessage email = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(email, true);
            helper.setTo(user.getEmail());
            helper.setFrom("vuducduy1112004@gmail.com", "HiringWire");
            helper.setSubject("Account Status Change Notification");
            helper.setText(message, true);
            mailSender.send(email);
        } catch (Exception e) {
            throw new HiringWireException("Failed to send email notification: " + e.getMessage());
        }
    }

    public List<UserResponse> getUsersForChat(String accountType, Long userId) {
        List<User> users;
        try {
            if ("ADMIN".equals(accountType)) {
                users = IUserRepository.findAll();
            } else if ("APPLICANT".equals(accountType)) {
                users = IUserRepository.findByAccountType(AccountType.EMPLOYER);
            } else if ("EMPLOYER".equals(accountType)) {
                users = IUserRepository.findByAccountType(AccountType.APPLICANT);
            } else {
                throw new IllegalArgumentException("Invalid account type: " + accountType);
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid account type: " + accountType, e);
        }
        return users.stream()
                .filter(user -> !user.getId().equals(userId))
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }
}