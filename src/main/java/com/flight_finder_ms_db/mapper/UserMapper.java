package com.flight_finder_ms_db.mapper;

import com.flight_finder_ms_db.dto.UserDTO;
import com.flight_finder_ms_db.dto.UserRegistration;
import com.flight_finder_ms_db.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }
        return new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getFavoriteDestinations()
        );
    }

    public User toEntity(UserRegistration dto) {
        if (dto == null) {
            return null;
        }
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setFavoriteDestinations(dto.getFavoriteDestinations());
        return user;
    }
}

