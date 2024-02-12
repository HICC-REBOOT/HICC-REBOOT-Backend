package hiccreboot.backend.domains.member.dto.response;

import hiccreboot.backend.domains.member.domain.Grade;
import hiccreboot.backend.domains.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class ProfileMemberResponse {
	private String name;
	private Grade grade;
	private String phoneNumber;
	private String department;
	private String email;

	public static ProfileMemberResponse create(Member member) {
		return ProfileMemberResponse.builder()
			.name(member.getName())
			.grade(member.getGrade())
			.phoneNumber(member.getPhoneNumber())
			.department(member.getDepartment().getName())
			.email(member.getEmail())
			.build();
	}

}
