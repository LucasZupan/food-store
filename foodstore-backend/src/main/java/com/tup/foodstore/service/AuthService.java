package com.tup.foodstore.service;

import com.tup.foodstore.dto.auth.AuthResponse;
import com.tup.foodstore.dto.auth.LoginRequest;
import com.tup.foodstore.dto.auth.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}