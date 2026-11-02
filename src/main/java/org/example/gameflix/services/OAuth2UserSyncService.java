package org.example.gameflix.services;

import org.example.gameflix.model.AppUser;
import org.example.gameflix.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

/** After a successful GitHub login, make sure a matching row exists in the users table. */
@Service
public class OAuth2UserSyncService extends DefaultOAuth2UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public OAuth2UserSyncService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String login = oAuth2User.getAttribute("login");
        if (email == null || email.isBlank()) {
            if (login == null) {
                throw new OAuth2AuthenticationException("no_login_found");
            }
            email = login + "@users.noreply.github.com";
        }
        final String finalEmail = email.toLowerCase();
        final String baseName = login != null ? login : finalEmail;

        repository.findByEmail(finalEmail).orElseGet(() -> {
            AppUser user = new AppUser();
            user.setEmail(finalEmail);
            user.setUsername(repository.existsByUsername(baseName)
                    ? baseName + "-" + UUID.randomUUID().toString().substring(0, 6)
                    : baseName);
            // Random password: this account signs in through GitHub, not the form.
            user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
            return repository.save(user);
        });

        return oAuth2User;
    }
}
