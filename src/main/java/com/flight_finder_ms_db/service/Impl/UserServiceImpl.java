package com.flight_finder_ms_db.service.Impl;

import com.flight_finder_ms_db.dto.LoginRequest;
import com.flight_finder_ms_db.dto.LoginResponse;
import com.flight_finder_ms_db.dto.UserDTO;
import com.flight_finder_ms_db.dto.UserRegistration;
import com.flight_finder_ms_db.entity.User;
import com.flight_finder_ms_db.mapper.UserMapper;
import com.flight_finder_ms_db.repository.UserRepository;
import com.flight_finder_ms_db.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDTO registerUser(UserRegistration userRegistration) {
        if (userRepository.existsByUsername(userRegistration.getUsername())) {
            throw new IllegalArgumentException("Username is already in use.");
        }

        if (userRepository.existsByEmail(userRegistration.getEmail())) {
            throw new IllegalArgumentException("Email is already in use.");
        }

        User user = userMapper.toEntity(userRegistration);
        User savedUser = userRepository.save(user);

        return userMapper.toDTO(savedUser);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));

        // Validación básica de contraseña (en texto plano por ahora)
        // TODO: Cuando se implemente JWT, usar BCrypt para comparar contraseñas encriptadas
        if (!user.getPassword().equals(loginRequest.getPassword())) {
            throw new IllegalArgumentException("Contraseña incorrecta.");
        }

        UserDTO userDTO = userMapper.toDTO(user);

        // Por ahora retorna null en el token, se implementará con JWT después
        return new LoginResponse("Login exitoso", userDTO);
    }
}

