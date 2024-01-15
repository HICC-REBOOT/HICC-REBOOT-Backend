package hiccreboot.backend.common.dto.Calender;

import hiccreboot.backend.domain.ScheduleType;

import java.time.LocalDate;
import java.util.List;

public record DayScheduleResponse(
        String name,
        Long scheduleId,
        List<LocalDate> dates,
        ScheduleType type,
        String content) {
}
