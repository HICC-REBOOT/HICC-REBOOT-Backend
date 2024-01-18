package hiccreboot.backend.common.dto.Calendar;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import hiccreboot.backend.domain.ScheduleType;

public record SimpleScheduleResponse(
	String name,
	Long scheduleId,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") LocalDate date,
	ScheduleType type
) {
}

