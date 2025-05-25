package com.oktayparlak.lottofun.controller;

import com.fasterxml.jackson.databind.ObjectMapper;


import com.oktayparlak.lottofun.business.abstracts.AuthService;
import com.oktayparlak.lottofun.dto.request.LoginRequest;
import com.oktayparlak.lottofun.dto.request.RegisterRequest;
import com.oktayparlak.lottofun.dto.response.AuthResponse;

import com.oktayparlak.lottofun.security.JwtAuthenticationFilter;
import com.oktayparlak.lottofun.security.JwtUtil;
import com.oktayparlak.lottofun.security.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;



@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    private final String USERNAME = "testuser";
    private final String PASSWORD = "testpassword";
    private final String EMAIL = "test@mail.com";

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    @Autowired
    public AuthControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    void register_shouldReturnAuthResponse() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest(USERNAME, PASSWORD, EMAIL);
        AuthResponse authResponse = new AuthResponse("token-example", USERNAME, EMAIL);

        Mockito.when(authService.register(registerRequest)).thenReturn(authResponse);

        mockMvc.perform(post("/auths/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());
    }

}
