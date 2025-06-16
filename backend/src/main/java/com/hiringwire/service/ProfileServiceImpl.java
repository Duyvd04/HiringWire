package com.hiringwire.service;

import java.util.ArrayList;
import java.util.List;

import com.hiringwire.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hiringwire.dto.ProfileDTO;
import com.hiringwire.dto.UserDTO;
import com.hiringwire.entity.Profile;
import com.hiringwire.entity.User;
import com.hiringwire.exception.HiringWireException;
import com.hiringwire.repository.IProfileRepository;

@Service("profileService")
public class ProfileServiceImpl implements ProfileService {

	@Autowired
	private IProfileRepository IProfileRepository;

	@Autowired
	private IUserRepository userRepository; // Add UserRepository

	@Override
	public Long createProfile(UserDTO userDTO) throws HiringWireException {
		Profile profile = new Profile();
		profile.setEmail(userDTO.getEmail());
		profile.setName(userDTO.getName());
		profile.setSkills(new ArrayList<>());
		profile.setExperiences(new ArrayList<>());
		profile.setCertifications(new ArrayList<>());
		Profile savedProfile = IProfileRepository.save(profile);
		return savedProfile.getId();
	}

	@Override
	public ProfileDTO getProfile(Long id) throws HiringWireException {
		Profile profile = IProfileRepository.findById(id)
				.orElseThrow(() -> new HiringWireException("PROFILE_NOT_FOUND"));
		ProfileDTO profileDTO = profile.toDTO();
		// Fetch accountType from User
		User user = userRepository.findByProfileId(id)
				.orElseThrow(() -> new HiringWireException("USER_NOT_FOUND_FOR_PROFILE_ID: " + id));
		profileDTO.setAccountType(user.getAccountType().name());
		return profileDTO;
	}

	@Override
	public ProfileDTO updateProfile(ProfileDTO profileDTO) throws HiringWireException {
		IProfileRepository.findById(profileDTO.getId())
				.orElseThrow(() -> new HiringWireException("PROFILE_NOT_FOUND"));
		IProfileRepository.save(profileDTO.toEntity());
		return profileDTO;
	}

	@Override
	public List<ProfileDTO> getAllProfiles() throws HiringWireException {
		try {
			return IProfileRepository.findAll().stream().map(profile -> {
				ProfileDTO profileDTO = profile.toDTO();
				// Fetch accountType from User
				userRepository.findByProfileId(profile.getId()).ifPresent(user ->
						profileDTO.setAccountType(user.getAccountType().name())
				);
				return profileDTO;
			}).toList();
		} catch (Exception e) {
			throw new HiringWireException("Failed to fetch profiles: " + e.getMessage());
		}
	}
}