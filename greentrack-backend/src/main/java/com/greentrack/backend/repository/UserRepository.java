package com.greentrack.backend.repository;

import com.greentrack.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // SELECT * FROM users WHERE username = ?
    Optional<User> findByUsername(String username);

    // verificar duplicados al registrar
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}