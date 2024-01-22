package hiccreboot.backend.common.dto.Calendar;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import hiccreboot.backend.domain.ScheduleDate;
import hiccreboot.backend.domain.ScheduleType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SimpleScheduleResponse {
	private final String name;
	private final Long scheduleId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private final LocalDate date;
	private final ScheduleType type;

	@Builder(access = AccessLevel.PRIVATE)
	private SimpleScheduleResponse(String name, Long scheduleId, LocalDate date, ScheduleType type) {
		this.name = name;
		this.scheduleId = scheduleId;
		this.date = date;
		this.type = type;
	}

	public static SimpleScheduleResponse create(ScheduleDate scheduleDate) {
		return SimpleScheduleResponse.builder()
			.name(scheduleDate.getSchedule().getName())
			.scheduleId(scheduleDate.getSchedule().getId())
			.date(LocalDate.of(scheduleDate.getYear(), scheduleDate.getMonth(), scheduleDate.getDayOfMonth()))
			.type(scheduleDate.getSchedule().getScheduleType())
			.build();
	}
}

