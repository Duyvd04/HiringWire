package com.hiringwire.entity;

import java.util.Base64;
import java.util.List;

import com.hiringwire.dto.Certification;
import com.hiringwire.dto.Experience;
import com.hiringwire.dto.ProfileDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "profiles")
public class Profile {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String email;
	private String jobTitle;
	private String company;
	private String location;
	private String about;
	@Lob
	@Column(name = "picture", columnDefinition = "LONGBLOB")
	private byte[] picture;
	private Long totalExp;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "profile_skills", joinColumns = @JoinColumn(name = "profile_id"))
	private List<String> skills;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "profile_experiences", joinColumns = @JoinColumn(name = "profile_id"))
	private List<Experience> experiences;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "profile_certifications", joinColumns = @JoinColumn(name = "profile_id"))
	private List<Certification> certifications;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "profile_saved_jobs", joinColumns = @JoinColumn(name = "profile_id"))
	private List<Long> savedJobs;
	private String accountType;

	public ProfileDTO toDTO() {
		return new ProfileDTO(
				this.id, this.name, this.email, this.jobTitle, this.company,
				this.location, this.about,
				this.picture != null ? Base64.getEncoder().encodeToString(this.picture) : null,
				this.totalExp, this.skills, this.experiences, this.certifications, this.savedJobs, this.accountType
		);
	}
}