package io.github.pauliustechin.egzaminas.feature.repository;

import io.github.pauliustechin.egzaminas.feature.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String email);
}
