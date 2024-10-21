package com.example.product_management.service;

import com.example.product_management.auth.AuthenticationRequest;
import com.example.product_management.auth.AuthenticationResponse;
import com.example.product_management.auth.RegisterRequest;
import com.example.product_management.model.entity.User;

public interface UserService {
    User save(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
}
