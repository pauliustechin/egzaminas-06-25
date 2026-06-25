package io.github.pauliustechin.egzaminas.security.request;

import io.github.pauliustechin.egzaminas.feature.dto.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterResponse {

    private Long userId;
    private String username;
    private UserRole role;

}
