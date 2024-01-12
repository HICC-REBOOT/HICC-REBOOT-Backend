package hiccreboot.backend.common.dto.Calender;

import java.time.LocalDate;
import java.util.List;

public record DayScheduleResponse(String name, Long scheduleId, List<LocalDate> dates, String type, String content) {
}
