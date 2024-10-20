package com.example.product_management.service;

import com.example.product_management.auth.AuthenticationRequest;
import com.example.product_management.auth.AuthenticationResponse;
import com.example.product_management.auth.RegisterRequest;
import com.example.product_management.config.JwtService;
import com.example.product_management.model.user.User;
import com.example.product_management.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public User save(RegisterRequest request){
        try{
            var user = User.builder()
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .role(request.getRole())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .build();

            return userRepository.save(user);
        }catch (Exception e){
            throw e;
        }

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(
                        ()-> new EntityNotFoundException(("User with username '" + request.getUsername() + "' not found"))
                );
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}
