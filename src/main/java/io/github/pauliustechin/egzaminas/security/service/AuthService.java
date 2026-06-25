package io.github.pauliustechin.egzaminas.security.service;

import io.github.pauliustechin.egzaminas.security.request.LoginRequest;
import io.github.pauliustechin.egzaminas.security.request.LoginSuccess;
import io.github.pauliustechin.egzaminas.security.request.RegisterRequest;
import io.github.pauliustechin.egzaminas.security.request.RegisterResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest request);

    LoginSuccess authenticate(LoginRequest request);
}
