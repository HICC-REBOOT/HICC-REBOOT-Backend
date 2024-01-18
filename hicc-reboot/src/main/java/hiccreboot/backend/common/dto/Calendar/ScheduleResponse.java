package hiccreboot.backend.common.dto.Calendar;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import hiccreboot.backend.domain.ScheduleType;

public record ScheduleResponse(
	String name,
	Long scheduleId,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") List<LocalDate> dates,
	ScheduleType type,
	String content) {
}
