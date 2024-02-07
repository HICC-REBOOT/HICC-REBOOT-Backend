package hiccreboot.backend.common.dto.Calendar;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import hiccreboot.backend.domain.ScheduleType;
import lombok.Getter;

@Getter
public class UpdateScheduleRequest {

	private String name;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime startDateTime;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime endDateTime;
	private ScheduleType type;
	private String content;
}
