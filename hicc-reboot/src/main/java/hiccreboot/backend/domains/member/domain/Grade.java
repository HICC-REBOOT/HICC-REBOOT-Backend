package hiccreboot.backend.domains.member.domain;

import lombok.Getter;

@Getter
public enum Grade {
	PRESIDENT("PRESIDENT", "회장"),
	EXECUTIVE("EXECUTIVE", "임원"),
	NORMAL("NORMAL", "일반"),
	APPLICANT("APPLICANT", "승인대기자");

	private final String description;
	private final String name;

	Grade(String description, String name) {
		this.description = description;
		this.name = name;
	}
}
