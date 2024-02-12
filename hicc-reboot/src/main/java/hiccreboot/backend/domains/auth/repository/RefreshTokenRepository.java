package hiccreboot.backend.domains.auth.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import hiccreboot.backend.domains.auth.domain.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
	Optional<RefreshToken> findByToken(String token);
}
