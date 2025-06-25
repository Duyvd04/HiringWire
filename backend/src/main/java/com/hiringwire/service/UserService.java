package com.hiringwire.service;

import com.hiringwire.dto.ResponseDTO;
import com.hiringwire.exception.HiringWireException;
import com.hiringwire.model.request.LoginRequest;
import com.hiringwire.model.request.UserRequest;
import com.hiringwire.model.response.UserResponse;

import java.util.List;


public interface UserService {

    UserResponse registerUser(UserRequest userRequest) throws HiringWireException;

    UserResponse getUserByEmail(String email) throws HiringWireException;

    UserResponse loginUser(LoginRequest loginRequest) throws HiringWireException;

    Boolean sendOTP(String email) throws Exception;

    Boolean verifyOtp(String email, String otp) throws HiringWireException;

    ResponseDTO changePassword(LoginRequest loginRequest) throws HiringWireException;

    List<UserResponse> getAllUsers() throws HiringWireException;

    void changeAccountStatus(Long id, String accountStatus) throws HiringWireException;

}
