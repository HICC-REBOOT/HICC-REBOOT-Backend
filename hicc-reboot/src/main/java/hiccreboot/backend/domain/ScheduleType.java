package hiccreboot.backend.domain;

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
}

