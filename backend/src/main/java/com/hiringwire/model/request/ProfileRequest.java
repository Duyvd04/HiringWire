package com.hiringwire.model.request;

import com.hiringwire.dto.Certification;
import com.hiringwire.dto.Experience;
import lombok.Data;

import java.util.List;

@Data
public class ProfileRequest {
    private Long id;
    private String name;
    private String email;
    private String jobTitle;
    private String company;
    private String location;
    private String about;
    private String picture;
    private Long totalExp;
    private List<String> skills;
    private List<Experience> experiences;
    private List<Certification> certifications;
    private List<Long> savedJobs;
    private String accountType;
}
