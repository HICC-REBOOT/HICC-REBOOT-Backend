package hiccreboot.backend.common.dto.Calendar;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import hiccreboot.backend.domain.ScheduleType;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UpdateScheduleRequest {

	private final Long scheduleId;

	private final String name;

	private final List<LocalDate> dates;

	private final ScheduleType type;

	private final String content;

	public UpdateScheduleRequest(Long scheduleId, String name, List<LocalDate> dates, ScheduleType type,
		String content) {
		this.scheduleId = scheduleId;
		this.name = name;
		this.dates = dates;
		this.type = type;
		this.content = content;
	}

}
