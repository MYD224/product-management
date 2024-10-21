package com.example.product_management.service;

import com.example.product_management.auth.AuthenticationRequest;
import com.example.product_management.auth.AuthenticationResponse;
import com.example.product_management.auth.RegisterRequest;
import com.example.product_management.config.JwtService;
import com.example.product_management.model.enums.Role;
import com.example.product_management.model.entity.User;
import com.example.product_management.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenValidRegisterRequest_thenUserIsSaved() {
        RegisterRequest request = new RegisterRequest("testuser", "test@example.com", Role.USER, "password");
        User user = User.builder()
                .username("testuser")
                .email("test@example.com")
                .role(Role.USER)
                .password("encodedPassword")
                .build();

        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.save(request);

        assertNotNull(savedUser);
        assertEquals("testuser", savedUser.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void whenAuthenticateWithValidCredentials_thenReturnToken() {
        AuthenticationRequest request = new AuthenticationRequest("testuser", "password");
        User user = User.builder()
                .username("testuser")
                .email("test@example.com")
                .role(Role.USER)
                .password("encodedPassword")
                .build();

        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwtToken");

        AuthenticationResponse response = userService.authenticate(request);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
    }

    @Test
    void whenAuthenticateWithInvalidUsername_thenThrowEntityNotFoundException() {
        AuthenticationRequest request = new AuthenticationRequest("invaliduser", "password");

        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.authenticate(request));
    }
}
