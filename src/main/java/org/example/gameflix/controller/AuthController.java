package org.example.gameflix.controller;

import org.example.gameflix.dto.RegistrationForm;
import org.example.gameflix.services.UserService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;
    private final ObjectProvider<ClientRegistrationRepository> oauthClients;

    public AuthController(UserService userService, ObjectProvider<ClientRegistrationRepository> oauthClients) {
        this.userService = userService;
        this.oauthClients = oauthClients;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("oauthEnabled", oauthClients.getIfAvailable() != null);
        return "login";
    }

    @GetMapping("/register")
    public String showRegistration(Model model) {
        model.addAttribute("form", new RegistrationForm());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("form") RegistrationForm form, Model model) {
        try {
            userService.register(form.getUsername(), form.getEmail(), form.getPassword());
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
        return "redirect:/login?registered";
    }
}
