package Nhom_06.HuaKieuLam.repositories;

import Nhom_06.HuaKieuLam.entities.User;
import Nhom_06.HuaKieuLam.entities.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    boolean existsByToken(String token);
    Optional<VerificationToken> findByUser(User user);

    Optional<VerificationToken> findByToken(String newToken);
}

