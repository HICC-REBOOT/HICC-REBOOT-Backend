package hiccreboot.backend.domains.auth.domain;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import hiccreboot.backend.domains.member.domain.Member;
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
@RedisHash(value = "password_reset", timeToLive = 60 * 10)
public class PasswordReset {
	@Id
	private Long id;

	@Indexed
	private String nonce;

	public static PasswordReset create(Member member, String nonce) {
		return PasswordReset.builder()
			.id(member.getId())
			.nonce(nonce)
			.build();
	}
}
