package com.hiringwire.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.hiringwire.model.enums.AccountStatus;
import com.hiringwire.model.enums.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hiringwire.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
	List<User> findByLastLoginDateBeforeAndAccountStatus(LocalDateTime date, AccountStatus status);
	Optional<User> findByProfileId(Long profileId);
	List<User> findByAccountType(AccountType accountType);
	boolean existsByEmail(String email);

}