package com.hiringwire.service;

import java.util.ArrayList;
import java.util.List;

import com.hiringwire.mapper.ProfileMapper;
import com.hiringwire.model.request.ProfileRequest;
import com.hiringwire.model.response.ProfileResponse;
import com.hiringwire.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.hiringwire.model.Profile;
import com.hiringwire.model.User;
import com.hiringwire.exception.HiringWireException;
import com.hiringwire.repository.ProfileRepository;

@Service("profileService")
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
	private final ProfileRepository IProfileRepository;
	private final UserRepository userRepository;
	private final ProfileMapper profileMapper;

	@Override
	public Long createProfile(ProfileRequest profileRequest) throws HiringWireException {
		Profile profile = profileMapper.toEntity(profileRequest);
		profile.setEmail(profile.getEmail());
		profile.setName(profile.getName());
		profile.setSkills(new ArrayList<>());
		profile.setExperiences(new ArrayList<>());
		profile.setCertifications(new ArrayList<>());
		Profile savedProfile = IProfileRepository.save(profile);
		return savedProfile.getId();
	}

	@Override
	public ProfileResponse getProfile(Long id) throws HiringWireException {
		Profile profile = IProfileRepository.findById(id)
				.orElseThrow(() -> new HiringWireException("PROFILE_NOT_FOUND"));
		ProfileResponse profileResponse = profileMapper.toResponse(profile);
		// Fetch accountType from User
		User user = userRepository.findByProfileId(id)
				.orElseThrow(() -> new HiringWireException("USER_NOT_FOUND_FOR_PROFILE_ID: " + id));
		profileResponse.setAccountType(user.getAccountType().name());
		return profileResponse;
	}

	@Override
	public ProfileResponse updateProfile(ProfileRequest profileRequest) throws HiringWireException {
		Long id = profileRequest.getId();
		if (id == null) {
			throw new HiringWireException("PROFILE_ID_REQUIRED");
		}

		Profile existing = IProfileRepository.findById(id)
				.orElseThrow(() -> new HiringWireException("PROFILE_NOT_FOUND"));

		Profile updated = profileMapper.toEntity(profileRequest);
		updated.setId(existing.getId());

		IProfileRepository.save(updated);

		ProfileResponse response = profileMapper.toResponse(updated);

		User user = userRepository.findByProfileId(updated.getId())
				.orElseThrow(() -> new HiringWireException("USER_NOT_FOUND_FOR_PROFILE_ID: " + updated.getId()));

		response.setAccountType(user.getAccountType().name());
		return response;
	}


	@Override
	public List<ProfileResponse> getAllProfiles() throws HiringWireException {
		try {
			return IProfileRepository.findAll().stream().map(profile -> {
				ProfileResponse response = profileMapper.toResponse(profile);
				// Fetch accountType from User
				userRepository.findByProfileId(profile.getId()).ifPresent(user ->
						response.setAccountType(user.getAccountType().name())
				);
				return response;
			}).toList();
		} catch (Exception e) {
			throw new HiringWireException("Failed to fetch profiles: " + e.getMessage());
		}
	}
}