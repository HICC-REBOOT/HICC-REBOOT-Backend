package hiccreboot.backend.common.dto.Calendar;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import hiccreboot.backend.domain.ScheduleType;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SimpleScheduleResponse {
	private final String name;
	private final Long scheduleId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private final LocalDate date;
	private final ScheduleType type;

}

