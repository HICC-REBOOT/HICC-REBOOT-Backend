package hiccreboot.backend.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hiccreboot.backend.common.exception.PasswordResetNotFoundException;
import hiccreboot.backend.domain.PasswordReset;
import hiccreboot.backend.repository.PasswordResetRepository;
import hiccreboot.backend.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

	private final MemberRepository memberRepository;
	private final PasswordResetRepository passwordResetRepository;
	private final PasswordEncoder passwordEncoder;

	public void resetPassword(String nonce, String password) {
		Long memberId = passwordResetRepository.findByNonce(nonce)
			.map(PasswordReset::getId)
			.orElseThrow(() -> PasswordResetNotFoundException.EXCEPTION);

		memberRepository.findById(memberId)
			.ifPresent(member -> {
				passwordResetRepository.deleteById(member.getId());

				member.updatePassword(password);
				member.passwordEncode(passwordEncoder);
			});
	}

}
