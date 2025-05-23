package com.oktayparlak.lottofun.business.abstracts;

import com.oktayparlak.lottofun.dto.response.UserResponse;

public interface UserService {
    UserResponse getCurrentUser(String email);
}
