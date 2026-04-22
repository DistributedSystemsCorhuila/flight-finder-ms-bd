package com.flight_finder_ms_db.controller;

import com.flight_finder_ms_db.dto.LoginRequest;
import com.flight_finder_ms_db.dto.LoginResponse;
import com.flight_finder_ms_db.dto.UserDTO;
import com.flight_finder_ms_db.dto.UserRegistration;
import com.flight_finder_ms_db.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsersControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UsersController usersController;

    private UserRegistration userRegistration;
    private UserDTO userDTO;
    private LoginRequest loginRequest;
    private LoginResponse loginResponse;

    @BeforeEach
    void setUp() {
        // Inicializar datos de prueba para registro
        userRegistration = new UserRegistration();
        userRegistration.setUsername("testuser");
        userRegistration.setEmail("test@example.com");
        userRegistration.setPassword("password123");

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

        // Inicializar respuesta de login
        loginResponse = new LoginResponse("Login exitoso", userDTO);
    }

    @Test
    void testRegisterUserSuccess() {
        // Arrange
        when(userService.registerUser(userRegistration)).thenReturn(userDTO);

        // Act
        ResponseEntity<?> result = usersController.registerUser(userRegistration);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertTrue(result.getBody() instanceof UserDTO);
        UserDTO responseBody = (UserDTO) result.getBody();
        assertEquals("testuser", responseBody.getUsername());
        assertEquals("Test", responseBody.getFirstName());
        assertEquals("User", responseBody.getLastName());
        assertEquals(1L, responseBody.getId());
        verify(userService, times(1)).registerUser(userRegistration);
    }

    @Test
    void testRegisterUserUsernameExists() {
        // Arrange
        when(userService.registerUser(userRegistration))
                .thenThrow(new IllegalArgumentException("Username is already in use."));

        // Act
        ResponseEntity<?> result = usersController.registerUser(userRegistration);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertEquals("Username is already in use.", result.getBody());
        verify(userService, times(1)).registerUser(userRegistration);
    }

    @Test
    void testRegisterUserEmailExists() {
        // Arrange
        when(userService.registerUser(userRegistration))
                .thenThrow(new IllegalArgumentException("Email is already in use."));

        // Act
        ResponseEntity<?> result = usersController.registerUser(userRegistration);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertEquals("Email is already in use.", result.getBody());
        verify(userService, times(1)).registerUser(userRegistration);
    }

    @Test
    void testLoginSuccess() {
        // Arrange
        when(userService.login(loginRequest)).thenReturn(loginResponse);

        // Act
        ResponseEntity<?> result = usersController.login(loginRequest);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertTrue(result.getBody() instanceof LoginResponse);
        LoginResponse responseBody = (LoginResponse) result.getBody();
        assertEquals("Login exitoso", responseBody.getMessage());
        assertNotNull(responseBody.getUser());
        assertEquals("testuser", responseBody.getUser().getUsername());
        verify(userService, times(1)).login(loginRequest);
    }

    @Test
    void testLoginUserNotFound() {
        // Arrange
        when(userService.login(loginRequest))
                .thenThrow(new IllegalArgumentException("Usuario no encontrado."));

        // Act
        ResponseEntity<?> result = usersController.login(loginRequest);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Usuario no encontrado.", result.getBody());
        verify(userService, times(1)).login(loginRequest);
    }

    @Test
    void testLoginIncorrectPassword() {
        // Arrange
        when(userService.login(loginRequest))
                .thenThrow(new IllegalArgumentException("Contraseña incorrecta."));

        // Act
        ResponseEntity<?> result = usersController.login(loginRequest);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Contraseña incorrecta.", result.getBody());
        verify(userService, times(1)).login(loginRequest);
    }

    @Test
    void testRegisterUserWithEmptyUsername() {
        // Arrange
        userRegistration.setUsername("");
        when(userService.registerUser(userRegistration))
                .thenThrow(new IllegalArgumentException("Username cannot be empty."));

        // Act
        ResponseEntity<?> result = usersController.registerUser(userRegistration);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        verify(userService, times(1)).registerUser(userRegistration);
    }

    @Test
    void testRegisterUserWithEmptyEmail() {
        // Arrange
        userRegistration.setEmail("");
        when(userService.registerUser(userRegistration))
                .thenThrow(new IllegalArgumentException("Email cannot be empty."));

        // Act
        ResponseEntity<?> result = usersController.registerUser(userRegistration);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        verify(userService, times(1)).registerUser(userRegistration);
    }

    @Test
    void testLoginWithEmptyUsername() {
        // Arrange
        loginRequest.setUsername("");
        when(userService.login(loginRequest))
                .thenThrow(new IllegalArgumentException("Usuario no encontrado."));

        // Act
        ResponseEntity<?> result = usersController.login(loginRequest);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        verify(userService, times(1)).login(loginRequest);
    }

    @Test
    void testLoginWithEmptyPassword() {
        // Arrange
        loginRequest.setPassword("");
        when(userService.login(loginRequest))
                .thenThrow(new IllegalArgumentException("Contraseña incorrecta."));

        // Act
        ResponseEntity<?> result = usersController.login(loginRequest);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        verify(userService, times(1)).login(loginRequest);
    }

    @Test
    void testRegisterMultipleUsersSuccessively() {
        // Arrange - Primer usuario
        when(userService.registerUser(userRegistration)).thenReturn(userDTO);

        // Act - Registrar primer usuario
        ResponseEntity<?> result1 = usersController.registerUser(userRegistration);

        // Assert
        assertEquals(HttpStatus.OK, result1.getStatusCode());

        // Arrange - Segundo usuario
        UserRegistration userRegistration2 = new UserRegistration();
        userRegistration2.setUsername("testuser2");
        userRegistration2.setEmail("test2@example.com");
        userRegistration2.setPassword("password456");

        UserDTO userDTO2 = new UserDTO();
        userDTO2.setId(2L);
        userDTO2.setUsername("testuser2");
        userDTO2.setFirstName("Test");
        userDTO2.setLastName("User2");

        when(userService.registerUser(userRegistration2)).thenReturn(userDTO2);

        // Act - Registrar segundo usuario
        ResponseEntity<?> result2 = usersController.registerUser(userRegistration2);

        // Assert
        assertEquals(HttpStatus.OK, result2.getStatusCode());
        UserDTO responseBody2 = (UserDTO) result2.getBody();
        assertEquals("testuser2", responseBody2.getUsername());
        verify(userService, times(1)).registerUser(userRegistration);
        verify(userService, times(1)).registerUser(userRegistration2);
    }

    @Test
    void testLoginMultipleUsersSuccessively() {
        // Arrange - Primer login
        when(userService.login(loginRequest)).thenReturn(loginResponse);

        // Act - Login del primer usuario
        ResponseEntity<?> result1 = usersController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.OK, result1.getStatusCode());

        // Arrange - Segundo login
        LoginRequest loginRequest2 = new LoginRequest();
        loginRequest2.setUsername("testuser2");
        loginRequest2.setPassword("password456");

        UserDTO userDTO2 = new UserDTO();
        userDTO2.setId(2L);
        userDTO2.setUsername("testuser2");
        userDTO2.setFirstName("Test");
        userDTO2.setLastName("User2");

        LoginResponse loginResponse2 = new LoginResponse("Login exitoso", userDTO2);
        when(userService.login(loginRequest2)).thenReturn(loginResponse2);

        // Act - Login del segundo usuario
        ResponseEntity<?> result2 = usersController.login(loginRequest2);

        // Assert
        assertEquals(HttpStatus.OK, result2.getStatusCode());
        LoginResponse responseBody2 = (LoginResponse) result2.getBody();
        assertEquals("testuser2", responseBody2.getUser().getUsername());
        verify(userService, times(1)).login(loginRequest);
        verify(userService, times(1)).login(loginRequest2);
    }

    @Test
    void testRegisterUserResponseNotNull() {
        // Arrange
        when(userService.registerUser(userRegistration)).thenReturn(userDTO);

        // Act
        ResponseEntity<?> result = usersController.registerUser(userRegistration);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void testLoginResponseNotNull() {
        // Arrange
        when(userService.login(loginRequest)).thenReturn(loginResponse);

        // Act
        ResponseEntity<?> result = usersController.login(loginRequest);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getStatusCode());
        assertNotNull(result.getBody());
    }
}

