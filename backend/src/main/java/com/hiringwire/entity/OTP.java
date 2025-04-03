package com.hiringwire.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "otp")
public class OTP {
    @Id
    @Column(unique = true)
    private String email;

    private String otpCode;
    private LocalDateTime creationTime;
}