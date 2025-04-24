package com.hiringwire.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import com.hiringwire.dto.JobDTO;
import com.hiringwire.dto.JobStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "jobs")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String jobTitle;

    private String company;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "job_id")
    private List<Applicant> applicants;

    private String about;
    private String experience;
    private String jobType;
    private String location;
    private Long packageOffered;
    private LocalDateTime postTime;
    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "job_skills_required", joinColumns = @JoinColumn(name = "job_id"))
    private List<String> skillsRequired;

    private JobStatus jobStatus;
    private Long postedBy;

    public JobDTO toDTO() {
        return new JobDTO(
                this.id,
                this.jobTitle,
                this.company,
                this.applicants != null ? this.applicants.stream().map(Applicant::toDTO).toList() : null,
                this.about,
                this.experience,
                this.jobType,
                this.location,
                this.packageOffered,
                this.postTime,
                this.description,
                this.skillsRequired,
                this.jobStatus,
                this.postedBy
        );
    }
}