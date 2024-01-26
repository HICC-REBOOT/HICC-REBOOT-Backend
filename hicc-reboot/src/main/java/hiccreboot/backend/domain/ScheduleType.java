package hiccreboot.backend.domain;

import java.util.Arrays;

import lombok.Getter;

@Getter
public enum ScheduleType {
	ACADEMIC("학술"),
	AMITY("친목"),
	SCHOOL_EVENT("학교행사"),
	ETC("기타");

	private final String name;

	ScheduleType(String name) {
		this.name = name;
	}

	public static ScheduleType findByName(String name) {
		return Arrays.stream(ScheduleType.values())
			.filter(menu -> menu.getName().equals(name))
			.findAny()
			.orElse(ETC);
	}
}

