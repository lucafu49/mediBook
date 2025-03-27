package com.example.mediBook.service.impl;


import com.example.mediBook.domain.*;
import com.example.mediBook.dto.*;
import com.example.mediBook.dto.request.RegisterUserRequest;
import com.example.mediBook.repository.*;
import com.example.mediBook.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDTO registerUser(RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email ya registrado");
        }

        Role.RoleType roleType;
        try {
            roleType = Role.RoleType.valueOf(request.getRole());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Rol inválido. Usar: PATIENT, DOCTOR o ADMIN");
        }

        Role role = roleRepository.findByRoleType(roleType)
                .orElseThrow(() -> new RuntimeException("Rol no configurado en la base de datos"));

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setRole(role);

        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    @Override
    public UserResponseDTO getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return convertToDTO(user);
    }

    private UserResponseDTO convertToDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole().getRoleType().name());
        return dto;
    }
}
