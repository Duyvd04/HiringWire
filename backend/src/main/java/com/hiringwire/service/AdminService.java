package com.hiringwire.service;

import com.hiringwire.model.User;
import com.hiringwire.exception.HiringWireException;
import java.util.List;

public interface AdminService {
    List<User> getPendingEmployers();
    void approveEmployer(Long id) throws HiringWireException;
    void rejectEmployer(Long id) throws HiringWireException;
//    void approveJob(Long id) throws HiringWireException;
//    List<Job> getPendingJobs();
}