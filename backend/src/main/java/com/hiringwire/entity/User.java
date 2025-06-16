package com.hiringwire.entity;

import com.hiringwire.dto.AccountStatus;
import com.hiringwire.dto.AccountType;
import com.hiringwire.dto.UserDTO;
import jakarta.persistence.*;
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

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "profile_id", referencedColumnName = "id")
	private Profile profile;

	private AccountStatus accountStatus;

	private LocalDateTime lastLoginDate;

	public UserDTO toDTO() {
		Long profileId = (profile != null) ? profile.getId() : null;
		return new UserDTO(this.id, this.name, this.email, null, this.accountType, profileId, this.accountStatus, this.lastLoginDate);
	}
}
