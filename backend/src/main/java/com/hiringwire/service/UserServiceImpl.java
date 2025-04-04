package com.hiringwire.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hiringwire.dto.LoginDTO;
import com.hiringwire.dto.NotificationDTO;
import com.hiringwire.dto.ResponseDTO;
import com.hiringwire.dto.UserDTO;
import com.hiringwire.entity.OTP;
import com.hiringwire.entity.User;
import com.hiringwire.exception.HiringWireException;
import com.hiringwire.repository.IOTPRepository;
import com.hiringwire.repository.IUserRepository;
import com.hiringwire.utility.Data;
import com.hiringwire.utility.Utilities;

import jakarta.mail.internet.MimeMessage;

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
	private Utilities utilities; // Add this

	@Override
	public UserDTO registerUser(UserDTO userDTO) throws HiringWireException {
		Optional<User> optional = IUserRepository.findByEmail(userDTO.getEmail());
		if (optional.isPresent())
			throw new HiringWireException("USER_FOUND");
		userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		userDTO.setProfileId(profileService.createProfile(userDTO));
		User user = IUserRepository.save(userDTO.toEntity());
		user.setPassword(null);
		return user.toDTO();
	}

	@Override
	public UserDTO loginUser(LoginDTO loginDTO) throws HiringWireException {
		User user = IUserRepository.findByEmail(loginDTO.getEmail())
				.orElseThrow(() -> new HiringWireException("USER_NOT_FOUND"));
		if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword()))
			throw new HiringWireException("INVALID_CREDENTIALS");
		user.setPassword(null);
		return user.toDTO();
	}

	@Override
	public Boolean sendOTP(String email) throws Exception {
		User user = IUserRepository.findByEmail(email)
				.orElseThrow(() -> new HiringWireException("USER_NOT_FOUND"));
		MimeMessage mm = mailSender.createMimeMessage();
		MimeMessageHelper message = new MimeMessageHelper(mm, true);
		message.setTo(email);
		message.setSubject("Your OTP Code");
		String generatedOtp = utilities.generateOTP(); // Updated to instance call
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
	public UserDTO getUserByEmail(String email) throws HiringWireException {
		return IUserRepository.findByEmail(email)
				.orElseThrow(() -> new HiringWireException("USER_NOT_FOUND")).toDTO();
	}
}