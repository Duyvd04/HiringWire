package com.hiringwire.repository;

import com.hiringwire.entity.Cv;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICvRepository extends JpaRepository<Cv,Long> {
    List<Cv> findByUserId(Long userId);
}
