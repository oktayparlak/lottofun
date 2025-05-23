package com.oktayparlak.lottofun.dto.converter;

import com.oktayparlak.lottofun.dto.response.UserResponse;
import com.oktayparlak.lottofun.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toResponse(User user);

}
