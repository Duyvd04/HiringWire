package com.hiringwire.model.response;

import com.hiringwire.model.enums.AccountStatus;
import com.hiringwire.model.enums.AccountType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private AccountType accountType;
    private AccountStatus accountStatus;
    private LocalDateTime lastLoginDate;
    private Long profileId;
}
