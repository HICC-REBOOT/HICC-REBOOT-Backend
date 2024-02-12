package hiccreboot.backend.domains.member.dto.response;

import hiccreboot.backend.domains.member.domain.Grade;
import hiccreboot.backend.domains.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberSimpleResponse {
	private final Long id;
	private final String name;
	private final Grade grade;

	@Builder(access = AccessLevel.PRIVATE)
	private MemberSimpleResponse(Long id, String name, Grade grade) {
		this.id = id;
		this.name = name;
		this.grade = grade;
	}

	public static MemberSimpleResponse create(Member member) {
		return MemberSimpleResponse.builder()
			.id(member.getId())
			.name(member.getName())
			.grade(member.getGrade())
			.build();
	}
}
