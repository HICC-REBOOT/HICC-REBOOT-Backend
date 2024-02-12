package hiccreboot.backend.domains.member.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import hiccreboot.backend.domains.member.domain.Grade;
import hiccreboot.backend.domains.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class MemberResponse {
	private final Long id;
	private final String department;
	private final String name;
	private final Grade grade;
	private final String studentNumber;
	private final String phoneNumber;
	@JsonFormat(pattern = "yyyy-MM-dd hh:mm")
	private final LocalDateTime approvedDate;

	public static MemberResponse create(Member member) {
		return MemberResponse.builder()
			.id(member.getId())
			.department(member.getDepartment().getName())
			.name(member.getName())
			.grade(member.getGrade())
			.studentNumber(member.getStudentNumber())
			.phoneNumber(member.getPhoneNumber())
			.approvedDate(member.getApprovedDate())
			.build();
	}
}
