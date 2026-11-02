package org.example.gameflix.services;

import org.example.gameflix.model.AppUser;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<AppUser> getAllUsers();

    /** Validates input and saves a new user. Throws IllegalArgumentException with a user-facing message. */
    AppUser register(String username, String email, String rawPassword);
    AppUser getUserByEmail(String email);

    Optional<AppUser> findByEmail(String email);
}
