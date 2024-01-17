package hiccreboot.backend.common.auth.jwt.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccessTokenAndRefreshTokenResponse {
	private final String accessToken;
	private final String refreshToken;
}
