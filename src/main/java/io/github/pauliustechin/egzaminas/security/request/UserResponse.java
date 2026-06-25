package io.github.pauliustechin.egzaminas.security.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserResponse {

    private Long userId;
    private String username;
    private List<String> roles;

}
