package hiccreboot.backend.dto.response;

import hiccreboot.backend.domain.Grade;
import hiccreboot.backend.domain.Member;
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

	public static ProfileMemberResponse create(Member member) {
		return ProfileMemberResponse.builder()
			.name(member.getName())
			.grade(member.getGrade())
			.phoneNumber(member.getPhoneNumber())
			.department(member.getDepartment().getName())
			.build();
	}

}
