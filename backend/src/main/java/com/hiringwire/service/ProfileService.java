package com.hiringwire.service;

import java.util.List;

import com.hiringwire.dto.ProfileDTO;
import com.hiringwire.dto.UserDTO;
import com.hiringwire.exception.HiringWireException;

public interface ProfileService {
	Long createProfile(UserDTO userDTO) throws HiringWireException;

	ProfileDTO getProfile(Long id) throws HiringWireException;

	ProfileDTO updateProfile(ProfileDTO profileDTO) throws HiringWireException;

	List<ProfileDTO> getAllProfiles() throws HiringWireException;

}
