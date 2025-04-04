package com.hiringwire.service;

import com.hiringwire.dto.LoginDTO;
import com.hiringwire.dto.ResponseDTO;
import com.hiringwire.dto.UserDTO;
import com.hiringwire.exception.HiringWireException;




public interface UserService {

	UserDTO registerUser(UserDTO userDTO) throws HiringWireException;
	UserDTO getUserByEmail(String email)throws HiringWireException;

	UserDTO loginUser(LoginDTO loginDTO) throws HiringWireException;

	Boolean sendOTP(String email) throws  Exception;

	Boolean verifyOtp( String email, String otp) throws HiringWireException;

	ResponseDTO changePassword( LoginDTO loginDTO) throws HiringWireException;
	
}
