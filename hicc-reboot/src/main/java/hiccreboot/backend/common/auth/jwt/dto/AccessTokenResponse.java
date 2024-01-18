package hiccreboot.backend.common.auth.jwt.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccessTokenResponse {
	private final String accessToken;
}
