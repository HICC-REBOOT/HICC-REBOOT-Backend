package hiccreboot.backend.common.dto.Calender;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class UpdateScheduleRequsetDTO {

    private final Long scheduleId;

    private final String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private final List<LocalDate> dates;

    private final String type;

    private final String content;

    public UpdateScheduleRequsetDTO(Long scheduleId, String name, List<LocalDate> dates, String type, String content) {
        this.scheduleId = scheduleId;
        this.name = name;
        this.dates = dates;
        this.type = type;
        this.content = content;
    }

}
