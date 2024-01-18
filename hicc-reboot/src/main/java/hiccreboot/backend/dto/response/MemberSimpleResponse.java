package hiccreboot.backend.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import hiccreboot.backend.domain.Grade;
import hiccreboot.backend.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
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
