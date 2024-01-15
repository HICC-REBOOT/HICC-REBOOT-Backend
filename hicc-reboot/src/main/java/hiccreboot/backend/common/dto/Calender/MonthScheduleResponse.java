package hiccreboot.backend.common.dto.Calender;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MonthScheduleResponse {
    private List<MonthScheduleElement> monthScheduleElements = new ArrayList<>();

    public void addElement(MonthScheduleElement monthScheduleElement) {
        monthScheduleElements.add(monthScheduleElement);
    }
}
