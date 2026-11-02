package org.example.gameflix.dto;

/** Safe public view of a user: no password hash, no card number. */
public record UserSummary(Long id, String username, String email) {
    public String getEmail() { return email; }
    public String getUsername() { return username; }

}
