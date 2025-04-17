package com.hiringwire.entity;

import com.hiringwire.dto.AccountStatus;
import jakarta.persistence.*;

import com.hiringwire.dto.AccountType;
import com.hiringwire.dto.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Column(unique = true)
	private String email;

	private String password;

	private AccountType accountType;

	private Long profileId;

	private AccountStatus accountStatus;

	private LocalDateTime lastLoginDate;


	public UserDTO toDTO() {
		return new UserDTO(this.id, this.name, this.email, this.password, this.accountType, this.profileId, this.accountStatus, this.lastLoginDate);
	}
}