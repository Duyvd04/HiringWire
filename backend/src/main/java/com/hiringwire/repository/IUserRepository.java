package com.hiringwire.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hiringwire.entity.User;

public interface IUserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
}