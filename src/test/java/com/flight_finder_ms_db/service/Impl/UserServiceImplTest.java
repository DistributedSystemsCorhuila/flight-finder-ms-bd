package com.flight_finder_ms_db.service.Impl;

import com.flight_finder_ms_db.dto.LoginRequest;
import com.flight_finder_ms_db.dto.LoginResponse;
import com.flight_finder_ms_db.dto.UserDTO;
import com.flight_finder_ms_db.dto.UserRegistration;
import com.flight_finder_ms_db.entity.User;
import com.flight_finder_ms_db.mapper.UserMapper;
import com.flight_finder_ms_db.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRegistration userRegistration;
    private User user;
    private UserDTO userDTO;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        // Inicializar datos de prueba para registro
        userRegistration = new UserRegistration();
        userRegistration.setUsername("testuser");
        userRegistration.setEmail("test@example.com");
        userRegistration.setPassword("password123");

        // Inicializar entidad User
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password123");

        // Inicializar DTO de usuario
        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("testuser");
        userDTO.setFirstName("Test");
        userDTO.setLastName("User");
        userDTO.setFavoriteDestinations("Paris");

        // Inicializar datos de prueba para login
        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");
    }

    @Test
    void testRegisterUserSuccess() {
        // Arrange
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(userMapper.toEntity(userRegistration)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        // Act
        UserDTO result = userService.registerUser(userRegistration);

        // Assert
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("Test", result.getFirstName());
        assertEquals("User", result.getLastName());
        assertEquals(1L, result.getId());
        verify(userRepository, times(1)).existsByUsername("testuser");
        verify(userRepository, times(1)).existsByEmail("test@example.com");
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).toEntity(userRegistration);
        verify(userMapper, times(1)).toDTO(user);
    }

    @Test
    void testRegisterUserUsernameAlreadyExists() {
        // Arrange
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(userRegistration)
        );

        assertEquals("Username is already in use.", exception.getMessage());
        verify(userRepository, times(1)).existsByUsername("testuser");
        verify(userRepository, never()).existsByEmail(any());
        verify(userRepository, never()).save(any());
        verify(userMapper, never()).toEntity(any());
    }

    @Test
    void testRegisterUserEmailAlreadyExists() {
        // Arrange
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(userRegistration)
        );

        assertEquals("Email is already in use.", exception.getMessage());
        verify(userRepository, times(1)).existsByUsername("testuser");
        verify(userRepository, times(1)).existsByEmail("test@example.com");
        verify(userRepository, never()).save(any());
        verify(userMapper, never()).toEntity(any());
    }

    @Test
    void testLoginSuccess() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        // Act
        LoginResponse result = userService.login(loginRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Login exitoso", result.getMessage());
        assertNotNull(result.getUser());
        assertEquals("testuser", result.getUser().getUsername());
        assertEquals("Test", result.getUser().getFirstName());
        verify(userRepository, times(1)).findByUsername("testuser");
        verify(userMapper, times(1)).toDTO(user);
    }

    @Test
    void testLoginUserNotFound() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.login(loginRequest)
        );

        assertEquals("Usuario no encontrado.", exception.getMessage());
        verify(userRepository, times(1)).findByUsername("testuser");
        verify(userMapper, never()).toDTO(any());
    }

    @Test
    void testLoginIncorrectPassword() {
        // Arrange
        loginRequest.setPassword("wrongpassword");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.login(loginRequest)
        );

        assertEquals("Contraseña incorrecta.", exception.getMessage());
        verify(userRepository, times(1)).findByUsername("testuser");
        verify(userMapper, never()).toDTO(any());
    }

    @Test
    void testLoginWithDifferentUser() {
        // Arrange
        User anotherUser = new User();
        anotherUser.setId(2L);
        anotherUser.setUsername("anotheruser");
        anotherUser.setEmail("another@example.com");
        anotherUser.setPassword("password456");

        UserDTO anotherUserDTO = new UserDTO();
        anotherUserDTO.setId(2L);
        anotherUserDTO.setUsername("anotheruser");
        anotherUserDTO.setFirstName("Another");
        anotherUserDTO.setLastName("User");

        LoginRequest anotherLoginRequest = new LoginRequest();
        anotherLoginRequest.setUsername("anotheruser");
        anotherLoginRequest.setPassword("password456");

        when(userRepository.findByUsername("anotheruser")).thenReturn(Optional.of(anotherUser));
        when(userMapper.toDTO(anotherUser)).thenReturn(anotherUserDTO);

        // Act
        LoginResponse result = userService.login(anotherLoginRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Login exitoso", result.getMessage());
        assertEquals("anotheruser", result.getUser().getUsername());
        verify(userRepository, times(1)).findByUsername("anotheruser");
    }

    @Test
    void testRegisterUserWithNullPassword() {
        // Arrange
        userRegistration.setPassword(null);
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(userMapper.toEntity(userRegistration)).thenReturn(user);
        when(userRepository.save(any())).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        // Act
        UserDTO result = userService.registerUser(userRegistration);

        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void testRegisterMultipleUsersSuccessively() {
        // Arrange - Primer usuario
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(userMapper.toEntity(userRegistration)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        // Act - Registrar primer usuario
        UserDTO result1 = userService.registerUser(userRegistration);

        // Assert
        assertNotNull(result1);

        // Arrange - Segundo usuario
        UserRegistration userRegistration2 = new UserRegistration();
        userRegistration2.setUsername("testuser2");
        userRegistration2.setEmail("test2@example.com");
        userRegistration2.setPassword("password456");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("testuser2");
        user2.setEmail("test2@example.com");
        user2.setPassword("password456");

        UserDTO userDTO2 = new UserDTO();
        userDTO2.setId(2L);
        userDTO2.setUsername("testuser2");
        userDTO2.setFirstName("Test");
        userDTO2.setLastName("User2");

        when(userRepository.existsByUsername("testuser2")).thenReturn(false);
        when(userRepository.existsByEmail("test2@example.com")).thenReturn(false);
        when(userMapper.toEntity(userRegistration2)).thenReturn(user2);
        when(userRepository.save(user2)).thenReturn(user2);
        when(userMapper.toDTO(user2)).thenReturn(userDTO2);

        // Act - Registrar segundo usuario
        UserDTO result2 = userService.registerUser(userRegistration2);

        // Assert
        assertNotNull(result2);
        assertEquals("testuser2", result2.getUsername());
        verify(userRepository, times(2)).save(any());
    }
}

