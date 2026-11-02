package org.example.gameflix.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class TokenController {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    public TokenController(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, JwtEncoder jwtEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    public record LoginRequest(String email, String password) {}

    @PostMapping("/token")
    public ResponseEntity<?> token(@RequestBody LoginRequest req) {
        try {
            UserDetails user = userDetailsService.loadUserByUsername(req.email());
            if (!passwordEncoder.matches(req.password(), user.getPassword())) {
                return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
            }
            Instant now = Instant.now();
            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .issuer("gameflix")
                    .subject(user.getUsername())
                    .issuedAt(now)
                    .expiresAt(now.plusSeconds(3600))
                    .build();
            JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
            String jwt = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
            return ResponseEntity.ok(Map.of("access_token", jwt, "token_type", "Bearer", "expires_in", 3600));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
    }
}