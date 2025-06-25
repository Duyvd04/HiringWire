package com.hiringwire.mapper;

import com.hiringwire.model.Notification;
import com.hiringwire.model.User;
import com.hiringwire.model.request.NotificationRequest;
import com.hiringwire.model.response.NotificationResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", expression = "java(user)")
    Notification toEntity(NotificationRequest request, @Context User user);

    @Mapping(target = "userId", source = "user.id")
    NotificationResponse toResponse(Notification notification);
}
