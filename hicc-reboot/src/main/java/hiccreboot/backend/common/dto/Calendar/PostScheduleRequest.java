package hiccreboot.backend.common.dto.Calendar;

import java.time.LocalDate;
import java.util.List;

import hiccreboot.backend.domain.ScheduleType;
import lombok.Getter;

@Getter
public class PostScheduleRequest {
	private final String name;

	private final List<LocalDate> dates;

	private final ScheduleType type;

	private final String content;

	public PostScheduleRequest(String name, List<LocalDate> dates, ScheduleType type, String content) {
		this.name = name;
		this.dates = dates;
		this.type = type;
		this.content = content;
	}
}
