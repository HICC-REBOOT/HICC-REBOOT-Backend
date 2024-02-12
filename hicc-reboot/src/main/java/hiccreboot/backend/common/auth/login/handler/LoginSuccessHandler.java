package hiccreboot.backend.common.auth.login.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import hiccreboot.backend.common.auth.jwt.TokenProvider;
import hiccreboot.backend.common.exception.MemberNotFoundException;
import hiccreboot.backend.domains.auth.domain.RefreshToken;
import hiccreboot.backend.domains.auth.repository.RefreshTokenRepository;
import hiccreboot.backend.domains.member.domain.Member;
import hiccreboot.backend.domains.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final TokenProvider tokenProvider;
	private final MemberRepository memberRepository;
	private final RefreshTokenRepository refreshTokenRepository;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException {
		String studentNumber = extractStudentNumber(authentication);

		String accessToken = tokenProvider.createAccessToken(studentNumber);
		String refreshToken = tokenProvider.createRefreshToken(studentNumber);

		tokenProvider.sendAccessAndRefreshToken(response, accessToken, refreshToken);
		tokenProvider.disableRefreshToken(studentNumber);

		RefreshToken token = RefreshToken.createRefreshToken(findMemberByStudentNumber(studentNumber),
			refreshToken);

		refreshTokenRepository.save(token);
		log.info("로그인 성공: {}", studentNumber);
		log.info("accessToken={}", accessToken);
		log.info("refreshToken={}", refreshToken);
	}

	private String extractStudentNumber(Authentication authentication) {
		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		return userDetails.getUsername();
	}

	private Member findMemberByStudentNumber(String studentNumber) {
		return memberRepository.findByStudentNumber(studentNumber)
			.orElseThrow(() -> MemberNotFoundException.EXCEPTION);
	}
}
