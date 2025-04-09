package ar.edu.uade.desa1.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import ar.edu.uade.desa1.domain.entity.PasswordResetToken;


import java.util.Optional;


 public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
}

