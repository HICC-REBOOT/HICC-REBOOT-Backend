package hiccreboot.backend.common.auth.jwt.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import hiccreboot.backend.common.auth.jwt.TokenProvider;
import hiccreboot.backend.common.exception.MemberNotFoundException;
import hiccreboot.backend.domain.Member;
import hiccreboot.backend.domain.RefreshToken;
import hiccreboot.backend.repository.member.MemberRepository;
import hiccreboot.backend.repository.refreshToken.RefreshTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final String LOGIN_URL = "/api/login";
	private static final String REFRESH_URL = "/api/refresh";

	private final TokenProvider tokenProvider;
	private final MemberRepository memberRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String accessToken = tokenProvider.extractAccessToken(request)
			.filter(tokenProvider::isValidToken)
			.orElse(null);

		String refreshToken = tokenProvider.extractRefreshToken(request)
			.filter(tokenProvider::isValidToken)
			.orElse(null);

		if (request.getRequestURI().equals(LOGIN_URL)) {
			filterChain.doFilter(request, response);
		} else if (request.getRequestURI().equals(REFRESH_URL) && refreshToken != null) {
			checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
		} else if (accessToken != null) {
			checkAccessTokenAndAuthentication(response, accessToken);
			filterChain.doFilter(request, response);
		} else {
			filterChain.doFilter(request, response);
		}
	}

	private void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
		refreshTokenRepository.findByToken(refreshToken)
			.map(RefreshToken::getId)
			.ifPresentOrElse(id -> {
				String studentNumber = findMember(id).getStudentNumber();
				String reIssuedAccessToken = tokenProvider.createAccessToken(studentNumber);
				tokenProvider.sendAccessToken(response, reIssuedAccessToken);
			}, () -> {
				log.info("액세스 토큰 재발급 실패");
			});
	}

	private void checkAccessTokenAndAuthentication(HttpServletResponse response, String accessToken) {
		tokenProvider.extractStudentNumber(accessToken)
			.flatMap(memberRepository::findByStudentNumber)
			.ifPresentOrElse(this::saveAuthentication,
				() -> {
					log.info("액세스 토큰 인증 실패");
				});

	}

	private void saveAuthentication(Member member) {
		UserDetails userDetails = User.builder()
			.username(member.getStudentNumber())
			.password(member.getPassword())
			.roles(member.getGrade().getDescription())
			.build();

		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
			authoritiesMapper.mapAuthorities(userDetails.getAuthorities()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	private Member findMember(Long id) {
		return memberRepository.findById(id)
			.orElseThrow(() -> MemberNotFoundException.EXCEPTION);
	}
}
