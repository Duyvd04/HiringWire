package com.hiringwire.mapper;

import com.hiringwire.model.Job;
import com.hiringwire.model.request.JobRequest;
import com.hiringwire.model.response.JobResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { ApplicantMapper.class })
public interface JobMapper {

    Job toEntity(JobRequest request);

    JobResponse toResponse(Job job);
}
