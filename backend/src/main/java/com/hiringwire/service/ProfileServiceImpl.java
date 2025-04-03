package com.hiringwire.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hiringwire.dto.ProfileDTO;
import com.hiringwire.dto.UserDTO;
import com.hiringwire.entity.Profile;
import com.hiringwire.exception.HiringWireException;
import com.hiringwire.repository.ProfileRepository;

@Service("profileService")
public class ProfileServiceImpl implements ProfileService {

	@Autowired
	private ProfileRepository profileRepository;

	@Override
	public Long createProfile(UserDTO userDTO) throws HiringWireException {
		Profile profile = new Profile();
		profile.setEmail(userDTO.getEmail());
		profile.setName(userDTO.getName());
		profile.setSkills(new ArrayList<>());
		profile.setExperiences(new ArrayList<>());
		profile.setCertifications(new ArrayList<>());
		Profile savedProfile = profileRepository.save(profile);
		return savedProfile.getId();
	}

	@Override
	public ProfileDTO getProfile(Long id) throws HiringWireException {
		return profileRepository.findById(id)
				.orElseThrow(() -> new HiringWireException("PROFILE_NOT_FOUND")).toDTO();
	}

	@Override
	public ProfileDTO updateProfile(ProfileDTO profileDTO) throws HiringWireException {
		profileRepository.findById(profileDTO.getId())
				.orElseThrow(() -> new HiringWireException("PROFILE_NOT_FOUND"));
		profileRepository.save(profileDTO.toEntity());
		return profileDTO;
	}

	@Override
	public List<ProfileDTO> getAllProfiles() throws HiringWireException {
		return profileRepository.findAll().stream().map((x) -> x.toDTO()).toList();
	}
}