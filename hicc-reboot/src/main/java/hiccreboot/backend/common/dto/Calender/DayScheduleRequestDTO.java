package hiccreboot.backend.common.dto.Calender;

import com.fasterxml.jackson.annotation.JsonFormat;
import hiccreboot.backend.domain.ScheduleType;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class DayScheduleRequestDTO {
    private final String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final List<LocalDate> dates;

    private final ScheduleType type;

    private final String content;

    public DayScheduleRequestDTO(String name, List<LocalDate> dates, ScheduleType type, String content) {
        this.name = name;
        this.dates = dates;
        this.type = type;
        this.content = content;
    }
}
