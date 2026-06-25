package io.github.pauliustechin.egzaminas.security.service;
import io.github.pauliustechin.egzaminas.exception.DuplicateUsernameException;
import io.github.pauliustechin.egzaminas.exception.UserAuthenticationException;
import io.github.pauliustechin.egzaminas.feature.dto.UserMapper;
import io.github.pauliustechin.egzaminas.feature.model.User;
import io.github.pauliustechin.egzaminas.feature.repository.UserRepository;
import io.github.pauliustechin.egzaminas.security.jwt.JwtUtils;
import io.github.pauliustechin.egzaminas.security.request.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder encoder;

    @Override
    public RegisterResponse register(RegisterRequest request) {

        if(userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateUsernameException(request.getUsername());
        }

        User user = userMapper.toEntity(request);
        user.setPassword(encoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);

        logger.info("User registered successfully: userId={}, username={}", user.getId(), user.getUsername());

        RegisterResponse response = userMapper.toResponse(savedUser);

        return response;
    }

    @Override
    @Transactional
    public LoginSuccess authenticate(LoginRequest request) {

        Authentication authentication;

        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (AuthenticationException exception) {
            throw new UserAuthenticationException("Invalid username or password");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        AuthResponse response = new AuthResponse(
                "Auth successful",
                Instant.now().plus(Duration.ofHours(jwtCookie.getMaxAge().toHours())),
                userDetails.getUsername(),
                userDetails.getId(),
                roles);

        logger.info("User logged in successfully: userId={}, username={}", userDetails.getId(), userDetails.getUsername());

        return new LoginSuccess(response, jwtCookie);
    }



}
