package com.hiringwire.api;

import com.hiringwire.exception.HiringWireException;
import com.hiringwire.model.request.ProfileRequest;
import com.hiringwire.model.response.ProfileResponse;
import com.hiringwire.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/profiles")
@Validated
@RequiredArgsConstructor
public class ProfileController {

	private final ProfileService profileService;

	@GetMapping("/get/{id}")
	public ResponseEntity<ProfileResponse> getProfile(@PathVariable Long id) throws HiringWireException {
		return new ResponseEntity<>(profileService.getProfile(id), HttpStatus.OK);
	}

	@GetMapping("/getAll")
	public ResponseEntity<List<ProfileResponse>> getAllProfiles() throws HiringWireException {
		return new ResponseEntity<>(profileService.getAllProfiles(), HttpStatus.OK);
	}

	@PutMapping("/update")
	public ResponseEntity<ProfileResponse> updateProfile(@RequestBody @Valid ProfileRequest profileRequest) throws HiringWireException {
		return new ResponseEntity<>(profileService.updateProfile(profileRequest), HttpStatus.OK);
	}
}