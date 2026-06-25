package io.github.pauliustechin.egzaminas.security.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank
    String username;

    @NotNull
    String password;

}
