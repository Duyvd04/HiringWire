package com.hiringwire.service;

import com.hiringwire.dto.LoginDTO;
import com.hiringwire.dto.ResponseDTO;
import com.hiringwire.dto.UserDTO;
import com.hiringwire.exception.HiringWireException;




public interface UserService {

	public UserDTO registerUser(UserDTO userDTO) throws HiringWireException;
	public UserDTO getUserByEmail(String email)throws HiringWireException;

	public UserDTO loginUser(LoginDTO loginDTO) throws HiringWireException;

	public Boolean sendOTP(String email) throws  Exception;

	public Boolean verifyOtp( String email, String otp) throws HiringWireException;

	public ResponseDTO changePassword( LoginDTO loginDTO) throws HiringWireException;
	
}
