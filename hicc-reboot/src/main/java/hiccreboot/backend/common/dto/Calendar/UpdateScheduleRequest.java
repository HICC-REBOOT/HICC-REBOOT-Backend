package hiccreboot.backend.common.dto.Calendar;

import java.time.LocalDate;
import java.util.List;

import hiccreboot.backend.domain.ScheduleType;
import lombok.Getter;

@Getter
public class UpdateScheduleRequest {

	private String name;
	private List<LocalDate> dates;
	private ScheduleType type;
	private String content;
}
