package com.hiringwire.api;

import com.hiringwire.dto.ResponseDTO;
import com.hiringwire.exception.HiringWireException;
import com.hiringwire.model.request.LoginRequest;
import com.hiringwire.model.request.UserRequest;
import com.hiringwire.model.response.UserResponse;
import com.hiringwire.service.UserService;
import com.hiringwire.service.UserServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/users")
@Validated
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final UserServiceImpl userServiceImpl;

	@PostMapping("/register")
	public ResponseEntity<UserResponse> registerUser(@RequestBody @Valid UserRequest request) throws HiringWireException {
		return new ResponseEntity<>(userService.registerUser(request), HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<UserResponse> loginUser(@RequestBody @Valid LoginRequest loginRequest) throws HiringWireException {
		return new ResponseEntity<>(userService.loginUser(loginRequest), HttpStatus.OK);
	}

	@PostMapping("/changePass")
	public ResponseEntity<ResponseDTO> changePassword(@RequestBody @Valid LoginRequest loginRequest) throws HiringWireException {
		return new ResponseEntity<>(userService.changePassword(loginRequest), HttpStatus.OK);
	}

	@PostMapping("/sendOtp/{email}")
	public ResponseEntity<ResponseDTO> sendOtp(@PathVariable @Email(message = "{user.email.invalid}") String email) throws Exception {
		userService.sendOTP(email);
		return new ResponseEntity<>(new ResponseDTO("OTP sent successfully."), HttpStatus.OK);
	}

	@GetMapping("/verifyOtp/{email}/{otp}")
	public ResponseEntity<ResponseDTO> verifyOtp(
			@PathVariable @NotBlank(message = "{user.email.absent}") @Email(message = "{user.email.invalid}") String email,
			@PathVariable @Pattern(regexp = "^[0-9]{6}$", message = "{otp.invalid}") String otp
	) throws HiringWireException {
		userService.verifyOtp(email, otp);
		return new ResponseEntity<>(new ResponseDTO("OTP has been verified."), HttpStatus.ACCEPTED);
	}

	@GetMapping("/chat/users/{userId}/{accountType}")
	public ResponseEntity<List<UserResponse>> getUsersForChat(@PathVariable Long userId, @PathVariable String accountType) {
		return ResponseEntity.ok(userServiceImpl.getUsersForChat(accountType, userId));
	}
}
