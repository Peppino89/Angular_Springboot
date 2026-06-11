package org.example.todo.repository;

import org.example.todo.entity.PasswordResetToken;
import org.example.todo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    void  deleteByUser(User user);
}
