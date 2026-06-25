package io.github.pauliustechin.egzaminas.security.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
public class AuthResponse {
    
    private String message;
    private Instant expiresAt;
    private String username;
    private Long userId;
    private List<String> roles;

}
