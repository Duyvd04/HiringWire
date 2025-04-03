package com.hiringwire.service;

import java.util.List;

import com.hiringwire.dto.ProfileDTO;
import com.hiringwire.dto.UserDTO;
import com.hiringwire.exception.HiringWireException;

public interface ProfileService {
	public Long createProfile(UserDTO userDTO) throws HiringWireException;

	public ProfileDTO getProfile(Long id) throws HiringWireException;

	public ProfileDTO updateProfile(ProfileDTO profileDTO) throws HiringWireException;

	public List<ProfileDTO> getAllProfiles() throws HiringWireException;
}
