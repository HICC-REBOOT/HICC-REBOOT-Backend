package hiccreboot.backend.controller;

import hiccreboot.backend.common.dto.Calender.*;
import hiccreboot.backend.common.exception.ScheduleNotFoundException;
import hiccreboot.backend.domain.Schedule;
import hiccreboot.backend.domain.ScheduleDate;
import hiccreboot.backend.service.CalenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/calender")
@RequiredArgsConstructor
public class CalenderController {

    private final CalenderService calenderService;

    @GetMapping("/month-schedule")
    public Object searchMonthSchedule(@RequestParam("year") int year, @RequestParam("month") int month) {
        List<ScheduleDate> scheduleDates = calenderService.findAllByMonth(year, month);

        MonthScheduleResponse monthScheduleResponses = new MonthScheduleResponse();
        scheduleDates.stream().forEach(scheduleDate -> {
            monthScheduleResponses.addElement(new MonthScheduleElement(scheduleDate.getSchedule().getName(), scheduleDate.getSchedule().getId(), LocalDate.of(scheduleDate.getYear(), scheduleDate.getMonth(), scheduleDate.getDayOfMonth()), scheduleDate.getSchedule().getScheduleType()));
        });

        return monthScheduleResponses;
    }

    @GetMapping("/schedule")
    public Object searchDaySchedule(@RequestParam("schedule-id") Long id) {
        Optional<Schedule> schedule = calenderService.findSchedule(id);

        if (schedule.isPresent()) {
            Schedule foundSchedule = schedule.get();

            List<LocalDate> localDates = new ArrayList<>();
            foundSchedule.getScheduleDates().stream().forEach(scheduleDate -> LocalDate.of(scheduleDate.getYear(), scheduleDate.getMonth(), scheduleDate.getDayOfMonth()));

            return new DayScheduleResponse(foundSchedule.getName(), foundSchedule.getId(), localDates, foundSchedule.getScheduleType(), foundSchedule.getContent());
        }
        throw new ScheduleNotFoundException();
    }

    //    @PostMapping("/schedule")
//    public Object addSchedule(PostDayScheduleDTO postDayScheduleDTO) {
//        calenderService.saveSchedule(postDayScheduleDTO);
//
//        // 이 부분 status에 맞게 변경
//        HashMap<String, Object> returnValues = new HashMap<>();
//        return returnValues;
//    }
    @PostMapping("/schedule")
    public Object addSchedule(DayScheduleRequestDTO dayScheduleRequestDTO) {
        calenderService.saveSchedule(dayScheduleRequestDTO.getName(), dayScheduleRequestDTO.getDates(), dayScheduleRequestDTO.getContent(), dayScheduleRequestDTO.getType());

        // 이 부분 status에 맞게 변경
        HashMap<String, Object> returnValues = new HashMap<>();
        returnValues.put("a", "b");
        return returnValues;
    }

    @DeleteMapping("schedule")
    public Object deleteSchedule(@RequestParam("schedule-id") Long id) {
        calenderService.deleteSchedule(id);

        // 이 부분 status에 맞게 변경
        HashMap<String, Object> returnValues = new HashMap<>();
        returnValues.put("a", "b");
        return returnValues;
    }

    @PatchMapping("schedule")
    public Object updateSchedule(UpdateScheduleRequsetDTO updateScheduleRequsetDTO) {
        calenderService.updateSchedule(updateScheduleRequsetDTO);

        // 이 부분 status에 맞게 변경
        HashMap<String, Object> returnValues = new HashMap<>();
        returnValues.put("a", "b");
        return returnValues;
    }
}
