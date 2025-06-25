package com.hiringwire.service;

import com.hiringwire.exception.HiringWireException;
import com.hiringwire.mapper.ProfileMapper;
import com.hiringwire.model.Profile;
import com.hiringwire.model.User;
import com.hiringwire.model.enums.AccountType;
import com.hiringwire.model.request.ProfileRequest;
import com.hiringwire.model.response.ProfileResponse;
import com.hiringwire.repository.ProfileRepository;
import com.hiringwire.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceImplTest {

    @Mock
    private ProfileRepository profileRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProfileMapper profileMapper;

    @InjectMocks
    private ProfileServiceImpl profileService;

    private Profile profile;
    private ProfileRequest profileRequest;
    private ProfileResponse profileResponse;
    private User user;

    @BeforeEach
    void setUp() {
        profile = new Profile();
        profile.setId(1L);
        profile.setName("Test User");
        profile.setEmail("test@example.com");

        profileRequest = new ProfileRequest();
        profileRequest.setName("Test User");
        profileRequest.setEmail("test@example.com");

        profileResponse = new ProfileResponse();
        profileResponse.setId(1L);
        profileResponse.setName("Test User");
        profileResponse.setEmail("test@example.com");

        user = new User();
        user.setAccountType(AccountType.APPLICANT);
        user.setProfile(profile);
    }

    @Test
    void createProfile_Success() throws HiringWireException {
        when(profileMapper.toEntity(any(ProfileRequest.class))).thenReturn(profile);
        when(profileRepository.save(any(Profile.class))).thenReturn(profile);

        Long result = profileService.createProfile(profileRequest);

        assertNotNull(result);
        assertEquals(profile.getId(), result);
        verify(profileRepository).save(any(Profile.class));
    }

    @Test
    void getProfile_Success() throws HiringWireException {
        when(profileRepository.findById(anyLong())).thenReturn(Optional.of(profile));
        when(userRepository.findByProfileId(anyLong())).thenReturn(Optional.of(user));
        when(profileMapper.toResponse(any(Profile.class))).thenReturn(profileResponse);

        ProfileResponse result = profileService.getProfile(1L);

        assertNotNull(result);
        assertEquals(profileResponse.getName(), result.getName());
        assertEquals(AccountType.APPLICANT.name(), result.getAccountType());
    }

    @Test
    void getProfile_NotFound() {
        when(profileRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(HiringWireException.class, () -> profileService.getProfile(1L));
    }


    @Test
    void getAllProfiles_Success() throws HiringWireException {
        List<Profile> profiles = Arrays.asList(profile);
        when(profileRepository.findAll()).thenReturn(profiles);
        when(profileMapper.toResponse(any(Profile.class))).thenReturn(profileResponse);
        when(userRepository.findByProfileId(anyLong())).thenReturn(Optional.of(user));

        List<ProfileResponse> results = profileService.getAllProfiles();

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(profileResponse.getName(), results.get(0).getName());
    }
}