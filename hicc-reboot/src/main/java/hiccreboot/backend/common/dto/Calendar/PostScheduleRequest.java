package hiccreboot.backend.common.dto.Calendar;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import hiccreboot.backend.domain.ScheduleType;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PostScheduleRequest {

	private String name;
	private List<LocalDate> dates;
	private ScheduleType type;
	private String content;
}
