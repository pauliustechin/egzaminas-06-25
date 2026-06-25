package io.github.pauliustechin.egzaminas.security.controller;

import io.github.pauliustechin.egzaminas.security.jwt.JwtUtils;
import io.github.pauliustechin.egzaminas.security.request.*;
import io.github.pauliustechin.egzaminas.security.service.AuthService;
import io.github.pauliustechin.egzaminas.security.service.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Tag(name = "Authentication APIs")
    @Operation(summary = "Register user")
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {

        RegisterResponse response = authService.register(request);

        return ResponseEntity.ok().body(response);

    }

    @Tag(name = "Authentication APIs")
    @Operation(summary = "Login user")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@Valid @RequestBody LoginRequest request) {

        LoginSuccess loginSuccess = authService.authenticate(request);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, loginSuccess.getCookie().toString())
                .body(loginSuccess.getResponse());

    }

    @Tag(name = "Authentication APIs", description = "APIs for authentication and session management")
    @Operation(summary = "Logout user")
    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout() {

        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new LogoutResponse("Logout successful."));

    }

    @Tag(name = "Authentication APIs")
    @Operation(summary = "Get authenticated user details")
    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .toList();

        UserResponse response = new UserResponse(userDetails.getId(), userDetails.getUsername(), roles);

        return ResponseEntity.ok().body(response);
    }

}
