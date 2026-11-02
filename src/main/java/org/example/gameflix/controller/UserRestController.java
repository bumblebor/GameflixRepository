package org.example.gameflix.controller;

import org.example.gameflix.dto.UserSummary;
import org.example.gameflix.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    /** Requires login. Returns id, username, email only. */
    @GetMapping
    public ResponseEntity<List<UserSummary>> getAllUsers() {
        List<UserSummary> users = userService.getAllUsers().stream()
                .map(u -> new UserSummary(u.getId(), u.getUsername(), u.getEmail()))
                .toList();
        return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
    }
    @GetMapping("/me")
    public ResponseEntity<UserSummary> getCurrentUser(Authentication authentication) {
        String email = resolveEmail(authentication);
        return userService.findByEmail(email)
                .map(u -> ResponseEntity.ok(new UserSummary(u.getId(), u.getUsername(), u.getEmail())))
                .orElse(ResponseEntity.notFound().build());
    }
    public String resolveEmail(Authentication auth) {
        if (auth.getPrincipal() instanceof OAuth2User oauthUser) {
            String email = oauthUser.getAttribute("email");
            if (email != null && !email.isBlank()) {
                return email;
            }
            String login = oauthUser.getAttribute("login");
            if (login != null) {
                return login + "@users.noreply.github.com";
            }
        }
        return auth.getName();
    }


}
