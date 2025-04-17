package com.hiringwire.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.hiringwire.dto.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hiringwire.entity.User;

public interface IUserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
	List<User> findByLastLoginDateBeforeAndAccountStatus(LocalDateTime date, AccountStatus status);

}