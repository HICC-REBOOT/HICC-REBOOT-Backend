package hiccreboot.backend.domain;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@RedisHash(value = "refresh", timeToLive = 60 * 60 * 24)
public class RefreshToken {
	@Id
	private Long id;
	@Indexed
	private String token;

	@Builder(access = AccessLevel.PRIVATE)
	private RefreshToken(Long id, String token) {
		this.id = id;
		this.token = token;
	}

	public static RefreshToken createRefreshToken(Member member, String token) {
		return RefreshToken.builder()
			.id(member.getId())
			.token(token)
			.build();
	}
}
