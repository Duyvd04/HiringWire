package com.hiringwire.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hiringwire.entity.Profile;

public interface IProfileRepository extends JpaRepository<Profile, Long> {
}