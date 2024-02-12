package hiccreboot.backend.domains.auth.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import hiccreboot.backend.domains.auth.domain.PasswordReset;

public interface PasswordResetRepository extends CrudRepository<PasswordReset, Long> {
	Optional<PasswordReset> findByNonce(String nonce);
}
