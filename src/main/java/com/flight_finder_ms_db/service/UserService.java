package com.flight_finder_ms_db.service;

import com.flight_finder_ms_db.dto.LoginRequest;
import com.flight_finder_ms_db.dto.LoginResponse;
import com.flight_finder_ms_db.dto.UserDTO;
import com.flight_finder_ms_db.dto.UserRegistration;

public interface UserService {

    UserDTO registerUser(UserRegistration userRegistration);

    LoginResponse login(LoginRequest loginRequest);
}

