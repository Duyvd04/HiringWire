package com.hiringwire.mapper;

import com.hiringwire.model.Profile;
import com.hiringwire.model.request.ProfileRequest;
import com.hiringwire.model.response.ProfileResponse;
import org.mapstruct.*;

import java.util.Base64;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "picture", expression = "java(fromBase64(request.getPicture()))")
    Profile toEntity(ProfileRequest request);

    @Mapping(target = "picture", expression = "java(toBase64(profile.getPicture()))")
    ProfileResponse toResponse(Profile profile);

    default String toBase64(byte[] data) {
        return data != null ? Base64.getEncoder().encodeToString(data) : null;
    }

    default byte[] fromBase64(String data) {
        return data != null ? Base64.getDecoder().decode(data) : null;
    }
}
