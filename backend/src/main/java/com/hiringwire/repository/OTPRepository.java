package com.hiringwire.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hiringwire.entity.OTP;

public interface OTPRepository extends JpaRepository<OTP, String> {
	List<OTP> findByCreationTimeBefore(LocalDateTime expiryTime);
}