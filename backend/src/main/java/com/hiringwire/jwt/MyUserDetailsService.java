package com.hiringwire.jwt;

import java.util.ArrayList;
import java.util.List;

import com.hiringwire.dto.AccountStatus;
import com.hiringwire.entity.User;
import com.hiringwire.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hiringwire.dto.UserDTO;
import com.hiringwire.exception.HiringWireException;
import com.hiringwire.service.UserService;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private IUserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		if (user.getAccountStatus() == AccountStatus.PENDING_APPROVAL) {
			throw new DisabledException("Account is pending approval");
		}

		if (user.getAccountStatus() == AccountStatus.BLOCKED) {
			throw new LockedException("Account is blocked");
		}

		if (user.getAccountStatus() == AccountStatus.REJECTED) {
			throw new DisabledException("Account has been rejected");
		}

		CustomUserDetails userDetails = new CustomUserDetails(
				user.getId(),
				user.getEmail(),
				user.getName(),
				user.getPassword(),
				user.getProfileId(),
				user.getAccountType(),
				List.of(new SimpleGrantedAuthority("ROLE_" + user.getAccountType().toString()))
		);

		return userDetails;
	}
}
