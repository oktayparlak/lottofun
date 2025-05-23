package com.oktayparlak.lottofun.business.concretes;

import com.oktayparlak.lottofun.business.abstracts.AuthService;
import com.oktayparlak.lottofun.dataAccess.UserRepository;
import com.oktayparlak.lottofun.dto.request.LoginRequest;
import com.oktayparlak.lottofun.dto.request.RegisterRequest;
import com.oktayparlak.lottofun.dto.response.AuthResponse;
import com.oktayparlak.lottofun.dto.response.UserResponse;
import com.oktayparlak.lottofun.entities.User;
import com.oktayparlak.lottofun.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository,
                           JwtUtil jwtUtil,
                           AuthenticationManager authenticationManager,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new RuntimeException("Invalid email or password");
        }
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        String token = jwtUtil.generateToken(user);
        return new AuthResponse(token, user.getUsername(), user.getEmail());
    }

    @Override
    public AuthResponse register(RegisterRequest registerRequest) {
        if(userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email already in use");
        }
        if(userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("Username already in use");
        }
        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build();
        userRepository.save(user);
        String token = jwtUtil.generateToken(user);
        return new AuthResponse(token, user.getUsername(), user.getEmail());
    }


}
