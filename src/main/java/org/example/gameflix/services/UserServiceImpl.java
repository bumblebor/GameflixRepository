package org.example.gameflix.services;

import org.example.gameflix.model.AppUser;
import org.example.gameflix.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final int MIN_PASSWORD_LENGTH = 8;

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<AppUser> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public AppUser register(String username, String email, String rawPassword) {
        username = username == null ? "" : username.trim();
        email = email == null ? "" : email.trim().toLowerCase();

        if (username.isEmpty()) {
            throw new IllegalArgumentException("Username is required.");
        }
        if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("Please enter a valid e-mail address.");
        }
        if (rawPassword == null || rawPassword.length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException("Password must be at least " + MIN_PASSWORD_LENGTH + " characters.");
        }
        if (repository.existsByEmail(email)) {
            throw new IllegalArgumentException("That e-mail is already registered.");
        }
        if (repository.existsByUsername(username)) {
            throw new IllegalArgumentException("That username is taken.");
        }

        AppUser user = new AppUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        return repository.save(user);
    }

    @Override
    public AppUser getUserByEmail(String email) {
        return repository.findByEmail(email.trim().toLowerCase()).orElseThrow(()->new RuntimeException("User Not Found"));
    }
    @Override
    public Optional<AppUser> findByEmail(String email) {
        return repository.findByEmail(email.trim().toLowerCase());
    }
}
