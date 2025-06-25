package com.hiringwire.model.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import com.hiringwire.model.enums.JobStatus;
import com.hiringwire.model.response.ApplicantResponse;

@Data
public class JobResponse {
    private Long id;
    private String jobTitle;
    private String company;
    private List<ApplicantResponse> applicants;
    private String about;
    private String experience;
    private String jobType;
    private String location;
    private Long salary;
    private LocalDateTime postTime;
    private String description;
    private List<String> skillsRequired;
    private JobStatus jobStatus;
    private Long postedBy;
}
