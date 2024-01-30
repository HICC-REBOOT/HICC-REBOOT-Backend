package hiccreboot.backend.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import hiccreboot.backend.domain.PasswordReset;

public interface PasswordResetRepository extends CrudRepository<PasswordReset, Long> {
	Optional<PasswordReset> findByNonce(String nonce);
}
