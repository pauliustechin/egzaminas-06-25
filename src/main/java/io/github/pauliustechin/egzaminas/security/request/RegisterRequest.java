package io.github.pauliustechin.egzaminas.security.request;

import io.github.pauliustechin.egzaminas.feature.user.dto.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(min = 6,max = 40)
    private String password;

    @NotNull
    private UserRole role;

}
