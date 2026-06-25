package io.github.pauliustechin.egzaminas.feature.dto;

import io.github.pauliustechin.egzaminas.feature.model.User;
import io.github.pauliustechin.egzaminas.security.request.RegisterRequest;
import io.github.pauliustechin.egzaminas.security.request.RegisterResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setRole(request.getRole());

        return user;
    }

    public RegisterResponse toResponse(User user) {
        return new RegisterResponse(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );
    }
}