package com.hiringwire.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hiringwire.exception.HiringWireException;
import com.hiringwire.mapper.UserMapper;
import com.hiringwire.model.Profile;
import com.hiringwire.model.User;
import com.hiringwire.model.enums.AccountStatus;
import com.hiringwire.model.enums.AccountType;
import com.hiringwire.model.request.NotificationRequest;
import com.hiringwire.repository.OTPRepository;
import com.hiringwire.repository.UserRepository;
import com.hiringwire.utility.Utilities;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UserServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class UserServiceImplDiffblueTest {
    @MockBean
    private JavaMailSender javaMailSender;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private OTPRepository oTPRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @MockBean
    private Utilities utilities;

    /**
     * Method under test: {@link UserServiceImpl#changeAccountStatus(Long, String)}
     */
    @Test
    void testChangeAccountStatus() throws HiringWireException, UnsupportedEncodingException {
        // Arrange
        Profile profile = new Profile();
        profile.setAbout("About");
        profile.setAccountType("3");
        profile.setCertifications(new ArrayList<>());
        profile.setCompany("Company");
        profile.setEmail("jane.doe@example.org");
        profile.setExperiences(new ArrayList<>());
        profile.setId(1L);
        profile.setJobTitle("Dr");
        profile.setLocation("Location");
        profile.setName("Name");
        profile.setPicture("AXAXAXAX".getBytes("UTF-8"));
        profile.setSavedJobs(new ArrayList<>());
        profile.setSkills(new ArrayList<>());
        profile.setTotalExp(1L);

        User user = new User();
        user.setAccountStatus(AccountStatus.ACTIVE);
        user.setAccountType(AccountType.APPLICANT);
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setLastLoginDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setName("Name");
        user.setPassword("iloveyou");
        user.setProfile(profile);
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(HiringWireException.class, () -> userServiceImpl.changeAccountStatus(1L, "3"));
        verify(userRepository).findById(eq(1L));
    }

    /**
     * Method under test: {@link UserServiceImpl#changeAccountStatus(Long, String)}
     */
    @Test
    void testChangeAccountStatus2() throws HiringWireException {
        // Arrange
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(HiringWireException.class, () -> userServiceImpl.changeAccountStatus(1L, "3"));
        verify(userRepository).findById(eq(1L));
    }

    /**
     * Method under test: {@link UserServiceImpl#changeAccountStatus(Long, String)}
     */
    @Test
    void testChangeAccountStatus3() throws HiringWireException, UnsupportedEncodingException, MailException {
        // Arrange
        Profile profile = new Profile();
        profile.setAbout("About");
        profile.setAccountType("3");
        profile.setCertifications(new ArrayList<>());
        profile.setCompany("Company");
        profile.setEmail("jane.doe@example.org");
        profile.setExperiences(new ArrayList<>());
        profile.setId(1L);
        profile.setJobTitle("Dr");
        profile.setLocation("Location");
        profile.setName("Name");
        profile.setPicture("AXAXAXAX".getBytes("UTF-8"));
        profile.setSavedJobs(new ArrayList<>());
        profile.setSkills(new ArrayList<>());
        profile.setTotalExp(1L);
        User user = mock(User.class);
        when(user.getEmail()).thenReturn("jane.doe@example.org");
        when(user.getId()).thenReturn(1L);
        when(user.getAccountStatus()).thenReturn(AccountStatus.ACTIVE);
        doNothing().when(user).setAccountStatus(Mockito.<AccountStatus>any());
        doNothing().when(user).setAccountType(Mockito.<AccountType>any());
        doNothing().when(user).setEmail(Mockito.<String>any());
        doNothing().when(user).setId(Mockito.<Long>any());
        doNothing().when(user).setLastLoginDate(Mockito.<LocalDateTime>any());
        doNothing().when(user).setName(Mockito.<String>any());
        doNothing().when(user).setPassword(Mockito.<String>any());
        doNothing().when(user).setProfile(Mockito.<Profile>any());
        user.setAccountStatus(AccountStatus.ACTIVE);
        user.setAccountType(AccountType.APPLICANT);
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setLastLoginDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setName("Name");
        user.setPassword("iloveyou");
        user.setProfile(profile);
        Optional<User> ofResult = Optional.of(user);

        Profile profile2 = new Profile();
        profile2.setAbout("About");
        profile2.setAccountType("3");
        profile2.setCertifications(new ArrayList<>());
        profile2.setCompany("Company");
        profile2.setEmail("jane.doe@example.org");
        profile2.setExperiences(new ArrayList<>());
        profile2.setId(1L);
        profile2.setJobTitle("Dr");
        profile2.setLocation("Location");
        profile2.setName("Name");
        profile2.setPicture("AXAXAXAX".getBytes("UTF-8"));
        profile2.setSavedJobs(new ArrayList<>());
        profile2.setSkills(new ArrayList<>());
        profile2.setTotalExp(1L);

        User user2 = new User();
        user2.setAccountStatus(AccountStatus.ACTIVE);
        user2.setAccountType(AccountType.APPLICANT);
        user2.setEmail("jane.doe@example.org");
        user2.setId(1L);
        user2.setLastLoginDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user2.setName("Name");
        user2.setPassword("iloveyou");
        user2.setProfile(profile2);
        when(userRepository.save(Mockito.<User>any())).thenReturn(user2);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        doNothing().when(javaMailSender).send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        doNothing().when(notificationService).sendNotification(Mockito.<NotificationRequest>any());

        // Act
        userServiceImpl.changeAccountStatus(1L, "ACTIVE");

        // Assert
        verify(user).getAccountStatus();
        verify(user).getEmail();
        verify(user).getId();
        verify(user, atLeast(1)).setAccountStatus(eq(AccountStatus.ACTIVE));
        verify(user).setAccountType(eq(AccountType.APPLICANT));
        verify(user).setEmail(eq("jane.doe@example.org"));
        verify(user).setId(eq(1L));
        verify(user).setLastLoginDate(isA(LocalDateTime.class));
        verify(user).setName(eq("Name"));
        verify(user).setPassword(eq("iloveyou"));
        verify(user).setProfile(isA(Profile.class));
        verify(notificationService).sendNotification(isA(NotificationRequest.class));
        verify(userRepository).findById(eq(1L));
        verify(userRepository).save(isA(User.class));
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(isA(MimeMessage.class));
    }

    /**
     * Method under test: {@link UserServiceImpl#changeAccountStatus(Long, String)}
     */
    @Test
    void testChangeAccountStatus4() throws HiringWireException, UnsupportedEncodingException {
        // Arrange
        Profile profile = new Profile();
        profile.setAbout("About");
        profile.setAccountType("3");
        profile.setCertifications(new ArrayList<>());
        profile.setCompany("Company");
        profile.setEmail("jane.doe@example.org");
        profile.setExperiences(new ArrayList<>());
        profile.setId(1L);
        profile.setJobTitle("Dr");
        profile.setLocation("Location");
        profile.setName("Name");
        profile.setPicture("AXAXAXAX".getBytes("UTF-8"));
        profile.setSavedJobs(new ArrayList<>());
        profile.setSkills(new ArrayList<>());
        profile.setTotalExp(1L);
        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(user.getAccountStatus()).thenReturn(AccountStatus.ACTIVE);
        doNothing().when(user).setAccountStatus(Mockito.<AccountStatus>any());
        doNothing().when(user).setAccountType(Mockito.<AccountType>any());
        doNothing().when(user).setEmail(Mockito.<String>any());
        doNothing().when(user).setId(Mockito.<Long>any());
        doNothing().when(user).setLastLoginDate(Mockito.<LocalDateTime>any());
        doNothing().when(user).setName(Mockito.<String>any());
        doNothing().when(user).setPassword(Mockito.<String>any());
        doNothing().when(user).setProfile(Mockito.<Profile>any());
        user.setAccountStatus(AccountStatus.ACTIVE);
        user.setAccountType(AccountType.APPLICANT);
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setLastLoginDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setName("Name");
        user.setPassword("iloveyou");
        user.setProfile(profile);
        Optional<User> ofResult = Optional.of(user);

        Profile profile2 = new Profile();
        profile2.setAbout("About");
        profile2.setAccountType("3");
        profile2.setCertifications(new ArrayList<>());
        profile2.setCompany("Company");
        profile2.setEmail("jane.doe@example.org");
        profile2.setExperiences(new ArrayList<>());
        profile2.setId(1L);
        profile2.setJobTitle("Dr");
        profile2.setLocation("Location");
        profile2.setName("Name");
        profile2.setPicture("AXAXAXAX".getBytes("UTF-8"));
        profile2.setSavedJobs(new ArrayList<>());
        profile2.setSkills(new ArrayList<>());
        profile2.setTotalExp(1L);

        User user2 = new User();
        user2.setAccountStatus(AccountStatus.ACTIVE);
        user2.setAccountType(AccountType.APPLICANT);
        user2.setEmail("jane.doe@example.org");
        user2.setId(1L);
        user2.setLastLoginDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user2.setName("Name");
        user2.setPassword("iloveyou");
        user2.setProfile(profile2);
        when(userRepository.save(Mockito.<User>any())).thenReturn(user2);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        doThrow(new HiringWireException("An error occurred")).when(notificationService)
                .sendNotification(Mockito.<NotificationRequest>any());

        // Act and Assert
        assertThrows(HiringWireException.class, () -> userServiceImpl.changeAccountStatus(1L, "ACTIVE"));
        verify(user).getAccountStatus();
        verify(user).getId();
        verify(user, atLeast(1)).setAccountStatus(eq(AccountStatus.ACTIVE));
        verify(user).setAccountType(eq(AccountType.APPLICANT));
        verify(user).setEmail(eq("jane.doe@example.org"));
        verify(user).setId(eq(1L));
        verify(user).setLastLoginDate(isA(LocalDateTime.class));
        verify(user).setName(eq("Name"));
        verify(user).setPassword(eq("iloveyou"));
        verify(user).setProfile(isA(Profile.class));
        verify(notificationService).sendNotification(isA(NotificationRequest.class));
        verify(userRepository).findById(eq(1L));
        verify(userRepository).save(isA(User.class));
    }
}
