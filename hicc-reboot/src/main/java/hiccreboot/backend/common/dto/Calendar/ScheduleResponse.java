package hiccreboot.backend.common.dto.Calendar;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import hiccreboot.backend.domain.ScheduleType;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ScheduleResponse {
	private final String name;
	private final Long scheduleId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private final List<LocalDate> dates;
	private final ScheduleType type;
	private final String content;

}
