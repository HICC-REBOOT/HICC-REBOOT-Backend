package hiccreboot.backend.domains.main.dto.response;

import hiccreboot.backend.domains.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FooterResponse {
	private final String name;
	private final String phoneNumber;

	public static FooterResponse create(Member member) {
		return FooterResponse.builder()
			.name(member.getName())
			.phoneNumber(member.getPhoneNumber())
			.build();
	}
}
