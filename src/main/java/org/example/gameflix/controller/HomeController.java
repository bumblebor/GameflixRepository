package org.example.gameflix.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            String name = authentication.getName();
            if (authentication.getPrincipal() instanceof OAuth2User oauthUser) {
                String email = oauthUser.getAttribute("email");
                String login = oauthUser.getAttribute("login");
                name = email != null ? email : (login != null ? login : name);
            }
            model.addAttribute("username", name);
        }
        return "index";
    }
}
