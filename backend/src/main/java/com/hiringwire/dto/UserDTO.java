//package com.hiringwire.dto;
//
//import com.hiringwire.model.enums.AccountStatus;
//import com.hiringwire.model.enums.AccountType;
//import com.hiringwire.model.User;
//import com.hiringwire.model.Profile;
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.Pattern;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class UserDTO {
//	private Long id;
//
//	@NotBlank(message = "{user.name.absent}")
//	private String name;
//
//	@NotBlank(message = "{user.email.absent}")
//	@Email(message = "{user.email.invalid}")
//	private String email;
//
//	@NotBlank(message = "{user.password.absent}")
//	@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,15}$", message = "{user.password.invalid}")
//	private String password;
//
//	private AccountType accountType;
//	private Long profileId;
//	private AccountStatus accountStatus;
//	private LocalDateTime lastLoginDate;
//
//	public User toEntity(Profile profile) {
//		return new User(this.id, this.name, this.email, this.password, this.accountType, profile, this.accountStatus, this.lastLoginDate);
//	}
//	public UserDTO(Long id, String name, String email, AccountType accountType) {
//		this.id = id;
//		this.name = name;
//		this.email = email;
//		this.accountType = accountType;
//	}
//}
