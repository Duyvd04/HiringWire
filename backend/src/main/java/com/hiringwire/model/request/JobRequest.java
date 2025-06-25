package com.hiringwire.model.request;

import lombok.Data;
import java.util.List;
import com.hiringwire.model.enums.JobStatus;

@Data
public class JobRequest {
    private Long id;
    private String jobTitle;
    private String company;
    private String about;
    private String experience;
    private String jobType;
    private String location;
    private Long salary;
    private String description;
    private List<String> skillsRequired;
    private JobStatus jobStatus;
    private Long postedBy;
}
