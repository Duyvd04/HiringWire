package com.hiringwire.service;


import com.hiringwire.exception.HiringWireException;
import com.hiringwire.mapper.UserMapper;
import com.hiringwire.model.Profile;
import com.hiringwire.model.User;
import com.hiringwire.model.enums.AccountStatus;
import com.hiringwire.model.enums.AccountType;
import com.hiringwire.model.request.LoginRequest;
import com.hiringwire.model.request.UserRequest;
import com.hiringwire.model.response.UserResponse;
import com.hiringwire.repository.OTPRepository;
import com.hiringwire.repository.UserRepository;
import com.hiringwire.service.NotificationService;
import com.hiringwire.service.UserServiceImpl;
import com.hiringwire.utility.Utilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private OTPRepository otpRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JavaMailSender mailSender;
    @Mock
    private NotificationService notificationService;
    @Mock
    private Utilities utilities;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRequest userRequest;
    private User user;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        userRequest = new UserRequest();
        userRequest.setEmail("test@example.com");
        userRequest.setPassword("password");
        userRequest.setName("Test User");
        userRequest.setAccountType(AccountType.APPLICANT);

        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        user.setAccountStatus(AccountStatus.ACTIVE);
        user.setProfile(new Profile());
        user.setLastLoginDate(LocalDateTime.now());

        userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setEmail("test@example.com");
    }

    @Test
    void registerUser_Success() throws HiringWireException {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userMapper.toEntity(any(UserRequest.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponse(any(User.class))).thenReturn(userResponse);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        UserResponse result = userService.registerUser(userRequest);

        assertNotNull(result);
        assertEquals(userResponse.getEmail(), result.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_EmailExists() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(HiringWireException.class, () -> userService.registerUser(userRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void loginUser_Success() throws HiringWireException {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponse(any(User.class))).thenReturn(userResponse);

        UserResponse result = userService.loginUser(loginRequest);

        assertNotNull(result);
        assertEquals(userResponse.getEmail(), result.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void loginUser_UserNotFound() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("nonexistent@example.com");
        loginRequest.setPassword("password");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(HiringWireException.class, () -> userService.loginUser(loginRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void loginUser_BlockedAccount() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        user.setAccountStatus(AccountStatus.BLOCKED);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        assertThrows(HiringWireException.class, () -> userService.loginUser(loginRequest));
        verify(userRepository, never()).save(any(User.class));
    }
}