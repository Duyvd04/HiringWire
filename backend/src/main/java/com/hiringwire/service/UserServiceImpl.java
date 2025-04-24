package com.hiringwire.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.hiringwire.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hiringwire.entity.OTP;
import com.hiringwire.entity.User;
import com.hiringwire.exception.HiringWireException;
import com.hiringwire.repository.IOTPRepository;
import com.hiringwire.repository.IUserRepository;
import com.hiringwire.utility.Data;
import com.hiringwire.utility.Utilities;

import jakarta.mail.internet.MimeMessage;
import org.springframework.transaction.annotation.Transactional;

@Service("userService")
public class UserServiceImpl implements UserService {
	@Autowired
	private IUserRepository IUserRepository;

	@Autowired
	private IOTPRepository IOTPRepository;

	@Autowired
	private ProfileService profileService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private Utilities utilities;

	@Override
	public UserDTO registerUser(UserDTO userDTO) throws HiringWireException {
		Optional<User> optional = IUserRepository.findByEmail(userDTO.getEmail());
		if (optional.isPresent())
			throw new HiringWireException("USER_FOUND");

		userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		userDTO.setProfileId(profileService.createProfile(userDTO));

		// Set initial status based on account type
		if (userDTO.getAccountType() == AccountType.EMPLOYER) {
			userDTO.setAccountStatus(AccountStatus.PENDING_APPROVAL);
		} else {
			userDTO.setAccountStatus(AccountStatus.ACTIVE);
		}

		userDTO.setLastLoginDate(LocalDateTime.now());
		User user = IUserRepository.save(userDTO.toEntity());
		user.setPassword(null);
		return user.toDTO();
	}

	@Override
	@Transactional
	public UserDTO loginUser(LoginDTO loginDTO) throws HiringWireException {
		User user = IUserRepository.findByEmail(loginDTO.getEmail())
				.orElseThrow(() -> new HiringWireException("USER_NOT_FOUND"));

		// Check account status first
		if (user.getAccountStatus() == AccountStatus.BLOCKED) {
			throw new HiringWireException("ACCOUNT_BLOCKED");
		}

		if (user.getAccountStatus() == AccountStatus.PENDING_APPROVAL) {
			throw new HiringWireException("ACCOUNT_PENDING_APPROVAL");
		}

		if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
			throw new HiringWireException("INVALID_CREDENTIALS");
		}

		user.setLastLoginDate(LocalDateTime.now());
		if (user.getAccountStatus() == AccountStatus.INACTIVE) {
			String oldStatus = user.getAccountStatus().toString();
			user.setAccountStatus(AccountStatus.ACTIVE);
			sendStatusChangeNotification(user, oldStatus, "ACTIVE",
					"Your account has been reactivated after successful login.");
		}

		user = IUserRepository.save(user);
		UserDTO userDTO = user.toDTO();
		userDTO.setPassword(null);
		return userDTO;
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
		OTP otp = new OTP(email, generatedOtp, LocalDateTime.now());
		IOTPRepository.save(otp);
		message.setText(Data.getMessageBody(generatedOtp, user.getName()), true);
		mailSender.send(mm);
		return true;
	}

	@Override
	public Boolean verifyOtp(String email, String otp) throws HiringWireException {
		OTP otpEntity = IOTPRepository.findById(email)
				.orElseThrow(() -> new HiringWireException("OTP_NOT_FOUND"));
		if (!otpEntity.getOtpCode().equals(otp))
			throw new HiringWireException("OTP_INCORRECT");
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
	public ResponseDTO changePassword(LoginDTO loginDTO) throws HiringWireException {
		User user = IUserRepository.findByEmail(loginDTO.getEmail())
				.orElseThrow(() -> new HiringWireException("USER_NOT_FOUND"));
		user.setPassword(passwordEncoder.encode(loginDTO.getPassword()));
		IUserRepository.save(user);
		NotificationDTO noti = new NotificationDTO();
		noti.setUserId(user.getId());
		noti.setMessage("Password Reset Successful");
		noti.setAction("Password Reset");
		notificationService.sendNotification(noti);
		return new ResponseDTO("Password changed successfully.");
	}


	@Override
	public List<UserDTO> getAllUsers() throws HiringWireException {
		List<User> users = IUserRepository.findAll();
		if (users.isEmpty())
			throw new HiringWireException("NO_USERS_FOUND");
		return users.stream().map((x) -> x.toDTO()).toList();
	}

	@Override
	public void changeAccountStatus(Long id, String accountStatus) throws HiringWireException {
		User user = IUserRepository.findById(id)
				.orElseThrow(() -> new HiringWireException("USER_NOT_FOUND"));

		String oldStatus = user.getAccountStatus().toString();
		String reason = "";

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
	public UserDTO getUserByEmail(String email) throws HiringWireException {
		return IUserRepository.findByEmail(email)
				.orElseThrow(() -> new HiringWireException("USER_NOT_FOUND")).toDTO();
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
		NotificationDTO notification = new NotificationDTO();
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

}