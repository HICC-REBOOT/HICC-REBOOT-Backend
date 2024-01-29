package hiccreboot.backend.common.auth.jwt;

import static hiccreboot.backend.common.consts.JwtConstants.*;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import hiccreboot.backend.common.auth.jwt.dto.AccessTokenAndRefreshTokenResponse;
import hiccreboot.backend.common.auth.jwt.dto.AccessTokenResponse;
import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.common.exception.MemberNotFoundException;
import hiccreboot.backend.common.util.ResponseWriter;
import hiccreboot.backend.domain.Member;
import hiccreboot.backend.domain.RefreshToken;
import hiccreboot.backend.repository.member.MemberRepository;
import hiccreboot.backend.repository.refreshToken.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenProviderImpl implements TokenProvider {

	@Value("${jwt.issuer}")
	private String issuer;

	@Value("${jwt.secret}")
	private String secretKey;

	@Value("${jwt.access.expiration}")
	private Long accessTokenExpirationPeriod;

	@Value("${jwt.refresh.expiration}")
	private Long refreshTokenExpirationPeriod;

	@Value("${jwt.access.header}")
	private String accessHeader;

	@Value("${jwt.refresh.header}")
	private String refreshHeader;

	private final MemberRepository memberRepository;
	private final RefreshTokenRepository refreshTokenRepository;

	@Override
	public String createAccessToken(String studentNumber) {
		Date now = new Date();
		Date expiration = new Date(now.getTime() + accessTokenExpirationPeriod);

		return Jwts.builder()
			.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
			.setSubject(ACCESS_TOKEN_SUBJECT)
			.setIssuedAt(now)
			.setExpiration(expiration)
			.setIssuer(issuer)
			.claim(STUDENT_NUMBER, studentNumber)
			.signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
			.compact();
	}

	@Override
	public String createRefreshToken(String studentNumber) {
		Date now = new Date();
		Date expiration = new Date(now.getTime() + refreshTokenExpirationPeriod);

		Member member = getMemberByStudentNumber(studentNumber);

		String issuedRefreshToken = Jwts.builder()
			.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
			.setSubject(REFRESH_TOKEN_SUBJECT)
			.setIssuedAt(now)
			.setExpiration(expiration)
			.setIssuer(issuer)
			.signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
			.compact();

		return refreshTokenRepository.save(RefreshToken.createRefreshToken(member, issuedRefreshToken)).getToken();
	}

	@Override
	public void disableRefreshToken(String studentNumber) {
		memberRepository.findByStudentNumber(studentNumber)
			.map(Member::getId)
			.ifPresent(refreshTokenRepository::deleteById);
	}

	@Override
	public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
		response.setStatus(HttpServletResponse.SC_OK);

		AccessTokenAndRefreshTokenResponse tokenResponse = AccessTokenAndRefreshTokenResponse.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();

		ResponseWriter.writeResponse(response, DataResponse.ok(tokenResponse));

	}

	@Override
	public void sendAccessToken(HttpServletResponse response, String accessToken) {
		response.setStatus(HttpServletResponse.SC_OK);

		AccessTokenResponse tokenResponse = AccessTokenResponse.builder()
			.accessToken(accessToken)
			.build();

		ResponseWriter.writeResponse(response, DataResponse.ok(tokenResponse));
	}

	@Override
	public Optional<String> extractAccessToken(HttpServletRequest request) {
		return Optional.ofNullable(request.getHeader(accessHeader))
			.filter(accessToken -> accessToken.startsWith(BEARER))
			.map(accessToken -> accessToken.replace(BEARER, ""));
	}

	@Override
	public Optional<String> extractRefreshToken(HttpServletRequest request) {
		return Optional.ofNullable(request.getHeader(refreshHeader))
			.filter(refreshToken -> refreshToken.startsWith(BEARER))
			.map(refreshToken -> refreshToken.replace(BEARER, ""));
	}

	@Override
	public Optional<String> extractStudentNumber(String accessToken) {
		try {
			return Optional.ofNullable(getClaims(accessToken).get(STUDENT_NUMBER, String.class));
		} catch (Exception e) {
			log.error("액세스 토큰이 유효하지 않습니다. token: {}", accessToken);
			return Optional.empty();
		}
	}

	@Override
	public Optional<String> extractStudentNumber(HttpServletRequest request) {
		return extractAccessToken(request).flatMap(this::extractStudentNumber);

	}

	@Override
	public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
		response.setHeader(accessHeader, accessToken);
	}

	@Override
	public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
		response.setHeader(refreshHeader, refreshToken);
	}

	@Override
	public boolean isValidToken(String token) {
		try {
			boolean tokenNotExpired = !getClaims(token).getExpiration().before(new Date());
			boolean connected;
			if (isAccessToken(token)) {
				String studentNumber = extractStudentNumber(token).get();
				connected = memberRepository.findByStudentNumber(studentNumber)
					.map(Member::getId)
					.flatMap(refreshTokenRepository::findById)
					.isPresent();
			} else {
				connected = refreshTokenRepository.findByToken(token)
					.isPresent();
			}
			return tokenNotExpired && connected;

		} catch (Exception e) {
			log.error(e.getMessage());
			log.error("토큰 검증 실패 : {}", token);
			return false;
		}
	}

	private Member getMemberByStudentNumber(String studentNumber) {
		return memberRepository.findByStudentNumber(studentNumber)
			.orElseThrow(MemberNotFoundException::new);
	}

	private Claims getClaims(String token) {
		return Jwts.parser()
			.setSigningKey(secretKey.getBytes())
			.parseClaimsJws(token)
			.getBody();
	}

	private boolean isAccessToken(String token) {
		Claims claims = getClaims(token);
		return claims.getSubject().equals(ACCESS_TOKEN_SUBJECT);
	}
}
