package hiccreboot.backend.domains.member.dto.response;

import hiccreboot.backend.domains.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class ApplicantResponse {
	private final Long id;
	private final String department;
	private final String name;

	public static ApplicantResponse create(Member member) {
		return ApplicantResponse.builder()
			.id(member.getId())
			.department(member.getDepartment().getName())
			.name(member.getName())
			.build();
	}
}
