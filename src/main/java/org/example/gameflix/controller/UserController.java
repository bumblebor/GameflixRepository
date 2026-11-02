package org.example.gameflix.controller;

import org.example.gameflix.dto.UserSummary;
import org.example.gameflix.model.AppUser;
import org.example.gameflix.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /** Requires login (see SecurityConfig). Shows only safe fields. */
    @GetMapping("/users")
    public String listUsers(Model model) {
        List<UserSummary> users = userService.getAllUsers().stream()
                .map(u -> new UserSummary(u.getId(), u.getUsername(), u.getEmail()))
                .toList();
        model.addAttribute("users", users);
        return "users";
    }
    @GetMapping("/user-page")
    public String getUsers(Authentication auth, Model model){
        if (auth.getPrincipal() instanceof OAuth2User oauthUser) {
            String email = oauthUser.getAttribute("email");
            model.addAttribute("user", email);
            return "users-page";
        }
        return "redirect:/";
    }
}
