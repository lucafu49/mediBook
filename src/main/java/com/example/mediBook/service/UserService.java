package com.example.mediBook.service;

import com.example.mediBook.dto.UserResponseDTO;
import com.example.mediBook.dto.request.RegisterUserRequest;

import java.util.UUID;

public interface UserService {
    UserResponseDTO registerUser(RegisterUserRequest request);
    UserResponseDTO getUserById(UUID id);
}
