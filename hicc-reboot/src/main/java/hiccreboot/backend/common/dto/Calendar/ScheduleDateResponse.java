package hiccreboot.backend.common.dto.Calendar;

import java.time.LocalDateTime;

import hiccreboot.backend.domain.Schedule;
import hiccreboot.backend.domain.ScheduleDate;
import hiccreboot.backend.domain.ScheduleType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class ScheduleDateResponse {
	private final String name;
	private final String content;
	private final ScheduleType type;
	private final Long scheduleId;
	private final LocalDateTime startDateTime;
	private final LocalDateTime endDateTime;

	public static ScheduleDateResponse create(ScheduleDate scheduleDate) {
		Schedule schedule = scheduleDate.getSchedule();

		return ScheduleDateResponse.builder()
			.name(schedule.getName())
			.content(schedule.getContent())
			.type(schedule.getScheduleType())
			.scheduleId(schedule.getId())
			.startDateTime(schedule.getStartDateTime())
			.endDateTime(schedule.getEndDateTime())
			.build();
	}
}
