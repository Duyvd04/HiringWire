package com.hiringwire.service;

import java.util.List;

import com.hiringwire.exception.HiringWireException;
import com.hiringwire.model.request.ProfileRequest;
import com.hiringwire.model.response.ProfileResponse;

public interface ProfileService {
	Long createProfile(ProfileRequest profileRequest) throws HiringWireException;

	ProfileResponse getProfile(Long id) throws HiringWireException;

	ProfileResponse updateProfile(ProfileRequest profileRequest) throws HiringWireException;

	List<ProfileResponse> getAllProfiles() throws HiringWireException;

}
