package com.oktayparlak.lottofun.business.abstracts;

import com.oktayparlak.lottofun.dto.request.LoginRequest;
import com.oktayparlak.lottofun.dto.request.RegisterRequest;
import com.oktayparlak.lottofun.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse login(LoginRequest loginRequest);

    AuthResponse register(RegisterRequest registerRequest);

}
