package com.flight_finder_ms_db.service;

import com.flight_finder_ms_db.dto.UserDTO;
import com.flight_finder_ms_db.dto.UserRegistration;

public interface UserService {

    UserDTO registerUser(UserRegistration userRegistration);
}

