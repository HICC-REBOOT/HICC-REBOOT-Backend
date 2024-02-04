package hiccreboot.backend.common.dto.Calendar;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import hiccreboot.backend.domain.Schedule;
import hiccreboot.backend.domain.ScheduleType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ScheduleResponse {
	private final String name;
	private final Long scheduleId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private final List<LocalDate> dates;
	private final ScheduleType type;
	private final String content;

	public static ScheduleResponse create(Schedule schedule) {
		List<LocalDate> dates = schedule.getScheduleDates().stream()
			.map(scheduleDate ->
				LocalDate.of(scheduleDate.getYear(), scheduleDate.getMonth(), scheduleDate.getDayOfMonth()))
			.toList();

		return new ScheduleResponse(schedule.getName(), schedule.getId(), dates, schedule.getScheduleType(),
			schedule.getContent());
	}
}
