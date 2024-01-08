package hiccreboot.backend.domain;

import lombok.Getter;

@Getter
public enum Grade {
	PRESIDENT("ROLE_PRESIDENT", "회장"),
	EXECUTIVE("ROLE_EXECUTIVE", "임원"),
	NORMAL("ROLE_NORMAL", "일반"),
	APPLICANT("ROLE_APPLICANT", "승인대기자");

	private final String description;
	private final String name;

	Grade(String description, String name) {
		this.description = description;
		this.name = name;
	}
}
