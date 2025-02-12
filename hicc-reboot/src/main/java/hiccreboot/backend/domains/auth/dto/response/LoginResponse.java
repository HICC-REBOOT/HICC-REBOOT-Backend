package hiccreboot.backend.domains.auth.dto.response;

public record LoginResponse(
	String accessToken,
	String refreshToken
) {
}
