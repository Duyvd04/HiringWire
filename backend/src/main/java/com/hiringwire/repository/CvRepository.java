package com.hiringwire.repository;

import com.hiringwire.model.Cv;
import com.hiringwire.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CvRepository extends JpaRepository<Cv,Long> {

    List<Cv> findByUser(User user);
}
