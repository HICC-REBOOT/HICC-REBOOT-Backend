package hiccreboot.backend.domains.calendar.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonFormat;

import hiccreboot.backend.domains.calendar.domain.Schedule;
import hiccreboot.backend.domains.calendar.domain.ScheduleDate;
import hiccreboot.backend.domains.calendar.domain.ScheduleType;
import lombok.Getter;

@Getter
public class ScheduleResponse {
	private final String name;
	private final Long scheduleId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private final LocalDateTime startDateTime;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private final LocalDateTime endDateTime;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private final List<LocalDate> dates;
	private final ScheduleType type;
	private final String content;

	private ScheduleResponse(String name, Long scheduleId, LocalDateTime startDateTime, LocalDateTime endDateTime,
		List<LocalDate> dates, ScheduleType type, String content) {
		this.name = name;
		this.scheduleId = scheduleId;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.dates = dates;
		this.type = type;
		this.content = content;
	}

	public static ScheduleResponse create(Schedule schedule) {
		return new ScheduleResponse(
			schedule.getName(),
			schedule.getId(),
			schedule.getStartDateTime(),
			schedule.getEndDateTime(),
			makeDates(schedule.getScheduleDates()),
			schedule.getScheduleType(),
			schedule.getContent()
		);
	}

	private static List<LocalDate> makeDates(List<ScheduleDate> scheduleDates) {
		return scheduleDates.stream()
			.map(scheduleDate -> LocalDate.of(scheduleDate.getYear(), scheduleDate.getMonth(),
				scheduleDate.getDayOfMonth()))
			.collect(Collectors.toList());
	}
}
