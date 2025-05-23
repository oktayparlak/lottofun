package com.oktayparlak.lottofun.business.concretes;

import com.oktayparlak.lottofun.business.abstracts.UserService;
import com.oktayparlak.lottofun.dataAccess.UserRepository;
import com.oktayparlak.lottofun.dto.converter.UserMapper;
import com.oktayparlak.lottofun.dto.response.UserResponse;
import com.oktayparlak.lottofun.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserResponse getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toResponse(user);
    }
}
