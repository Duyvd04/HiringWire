package com.hiringwire.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hiringwire.model.OTP;

public interface OTPRepository extends JpaRepository<OTP, Long> {
	List<OTP> findByCreationTimeBefore(LocalDateTime expiryTime);
	Optional<OTP> findByUserEmail(String email);

}