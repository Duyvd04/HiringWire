package com.hiringwire.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hiringwire.model.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}