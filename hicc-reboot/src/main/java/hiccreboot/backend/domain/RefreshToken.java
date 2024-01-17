package hiccreboot.backend.domain;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "refresh", timeToLive = 60 * 60 * 24)
public class RefreshToken {
	@Id
	private Long id;
	@Indexed
	private String token;

	public static RefreshToken createRefreshToken(Member member, String token) {
		return RefreshToken.builder()
			.id(member.getId())
			.token(token)
			.build();
	}
}
