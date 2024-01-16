package hiccreboot.backend.common.auth.jwt;

import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface TokenProvider {
	String createAccessToken(String studentNumber);

	String createRefreshToken(String studentNumber);

	void disableRefreshToken(String studentNumber);

	void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken);

	void sendAccessToken(HttpServletResponse response, String accessToken);

	Optional<String> extractAccessToken(HttpServletRequest request);

	Optional<String> extractRefreshToken(HttpServletRequest request);

	Optional<String> extractStudentNumber(String accessToken);

	Optional<String> extractStudentNumber(HttpServletRequest request);

	void setAccessTokenHeader(HttpServletResponse response, String accessToken);

	void setRefreshTokenHeader(HttpServletResponse response, String refreshToken);

	boolean isValidToken(String token);
}
