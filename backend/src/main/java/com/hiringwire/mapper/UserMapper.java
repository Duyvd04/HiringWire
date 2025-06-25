package com.hiringwire.mapper;

import com.hiringwire.model.User;
import com.hiringwire.model.Profile;
import com.hiringwire.model.request.UserRequest;
import com.hiringwire.model.response.UserResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "profile", ignore = true)
    @Mapping(target = "accountStatus", ignore = true)
    @Mapping(target = "lastLoginDate", ignore = true)
    User toEntity(UserRequest request);

    @Mapping(source = "profile.id", target = "profileId")
    UserResponse toResponse(User user);
}
