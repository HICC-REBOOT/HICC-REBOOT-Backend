package hiccreboot.backend.common.dto.Calendar;

import java.time.LocalDate;
import java.util.List;

import hiccreboot.backend.domain.ScheduleType;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class PostScheduleRequest {
	private String name;

	private List<LocalDate> dates;

	private ScheduleType type;

	private String content;
}
