package hiccreboot.backend.repository.refreshToken;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import hiccreboot.backend.domain.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
	Optional<RefreshToken> findByToken(String token);
}
