package hiccreboot.backend.common.dto.Calender;

import hiccreboot.backend.domain.ScheduleType;

import java.time.LocalDate;

public record MonthScheduleElement(
        String name,
        Long scheduleId,
        LocalDate date,
        ScheduleType type
) {
}

