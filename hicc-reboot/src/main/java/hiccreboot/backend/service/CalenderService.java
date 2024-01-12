package hiccreboot.backend.service;

import hiccreboot.backend.common.dto.Calender.UpdateScheduleRequsetDTO;
import hiccreboot.backend.common.exception.ScheduleNotFoundException;
import hiccreboot.backend.domain.Schedule;
import hiccreboot.backend.domain.ScheduleDate;
import hiccreboot.backend.domain.ScheduleType;
import hiccreboot.backend.repository.ScheduleDateRepository;
import hiccreboot.backend.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CalenderService {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleDateRepository scheduleDateRepository;

    public List<ScheduleDate> findAllByMonth(int year, int month) {
        return scheduleDateRepository.findAllByYearAndMonth(year, month);

    }

    public Optional<Schedule> findSchedule(Long id) {
        return scheduleRepository.findById(id);
    }

    public Schedule saveSchedule(String name, List<LocalDate> dates, String content, String type) {
        Schedule schedule = Schedule.createSchedule(
                name,
                content,
                ScheduleType.findByName(type)
        );

        dates.stream()
                .forEach(date -> {
                    ScheduleDate scheduleDate = ScheduleDate.createScheduleDate(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
                    scheduleDate.addSchedule(schedule);
                });
        scheduleRepository.save(schedule);

        return schedule;
    }

    public void deleteSchedule(Long id) {
        try {
            scheduleRepository.deleteById(id);
        } catch (IllegalArgumentException e) {
            throw new ScheduleNotFoundException();
        }
    }

    public Schedule updateSchedule(UpdateScheduleRequsetDTO updateScheduleRequsetDTO) {
        deleteSchedule(updateScheduleRequsetDTO.getScheduleId());
        return saveSchedule(updateScheduleRequsetDTO.getName(), updateScheduleRequsetDTO.getDates(), updateScheduleRequsetDTO.getContent(), updateScheduleRequsetDTO.getType());

//        Optional<Schedule> schedule = scheduleRepository.findById(updateScheduleRequsetDTO.getScheduleId());

//        if (schedule.isPresent()) {
//            Schedule updatedSchedule = schedule.get();
//            updatedSchedule.updateName(updateScheduleRequsetDTO.getName());
//            updatedSchedule.updateContent(updateScheduleRequsetDTO.getContent());
//            updatedSchedule.updateScheduleType(ScheduleType.findByName(updateScheduleRequsetDTO.getType()));
//        }
//        throw new ScheduleNotFoundException();
    }
}
