package com.hiringwire.mapper;

import com.hiringwire.model.Applicant;
import com.hiringwire.model.User;
import com.hiringwire.model.request.ApplicantRequest;
import com.hiringwire.model.response.ApplicantResponse;
import org.mapstruct.*;

import java.util.Base64;

@Mapper(componentModel = "spring")
public interface ApplicantMapper {

    @Mapping(target = "resume", expression = "java(fromBase64(request.getResume()))")
    @Mapping(target = "extractedResume", expression = "java(fromBase64(request.getExtractedResume()))")
    @Mapping(target = "user", ignore = true) // set manually in service
    @Mapping(target = "job", ignore = true)  // set manually in service
    Applicant toEntity(ApplicantRequest request);

    @Mapping(source = "user.id", target = "applicantId")
    @Mapping(source = "user.name", target = "name")
    @Mapping(source = "user.email", target = "email")
    @Mapping(target = "resume", expression = "java(toBase64(applicant.getResume()))")
    @Mapping(target = "extractedResume", expression = "java(toBase64(applicant.getExtractedResume()))")
    ApplicantResponse toResponse(Applicant applicant);

    default String toBase64(byte[] data) {
        return data != null ? Base64.getEncoder().encodeToString(data) : null;
    }

    default byte[] fromBase64(String data) {
        return data != null ? Base64.getDecoder().decode(data) : null;
    }
}
