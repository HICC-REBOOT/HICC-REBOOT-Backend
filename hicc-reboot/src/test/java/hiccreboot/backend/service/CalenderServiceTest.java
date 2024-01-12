package hiccreboot.backend.service;

import hiccreboot.backend.common.dto.Calender.DayScheduleRequestDTO;
import hiccreboot.backend.domain.Schedule;
import hiccreboot.backend.domain.ScheduleDate;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class CalenderServiceTest {

    @Autowired
    CalenderService calenderService;

    @Autowired
    EntityManager em;

    @Test
    void findAllByMonth() {
        //given
        int year = 2023;
        int month = 12;

        List<LocalDate> localDates = new ArrayList<>();
        localDates.add(LocalDate.of(2023, 12, 13));
        localDates.add(LocalDate.of(2023, 12, 14));

        DayScheduleRequestDTO dayScheduleRequestDTO = new DayScheduleRequestDTO("윤찬호", localDates, "학술", "안녕나야");

        //when
        Schedule schedule = calenderService.saveSchedule(
                dayScheduleRequestDTO.getName(),
                dayScheduleRequestDTO.getDates(),
                dayScheduleRequestDTO.getContent(),
                dayScheduleRequestDTO.getType());

        em.flush();

        //then
        List<ScheduleDate> scheduleDates = calenderService.findAllByMonth(year, month);
        Assertions.assertEquals(schedule.getId(), scheduleDates.get(0).getSchedule().getId());


    }

    @Test
    void findSchedule() {
        //given
        int year = 2023;
        int month = 12;

        List<LocalDate> localDates = new ArrayList<>();
        localDates.add(LocalDate.of(2023, 12, 13));
        localDates.add(LocalDate.of(2023, 12, 14));

        DayScheduleRequestDTO dayScheduleRequestDTO = new DayScheduleRequestDTO("윤찬호", localDates, "학술", "안녕나야");

        //when
        Schedule schedule = calenderService.saveSchedule(
                dayScheduleRequestDTO.getName(),
                dayScheduleRequestDTO.getDates(),
                dayScheduleRequestDTO.getContent(),
                dayScheduleRequestDTO.getType());

        em.flush();

        Optional<Schedule> schedule1 = calenderService.findSchedule(schedule.getId());

        //then
        Assertions.assertEquals(schedule.getId(), schedule1.get().getId());

    }

//    @Test
//    void saveSchedule() {
//    }

    @Test
    void deleteSchedule() {
        //given
        int year = 2023;
        int month = 12;

        List<LocalDate> localDates = new ArrayList<>();
        localDates.add(LocalDate.of(2023, 12, 13));
        localDates.add(LocalDate.of(2023, 12, 14));

        DayScheduleRequestDTO dayScheduleRequestDTO = new DayScheduleRequestDTO("윤찬호", localDates, "학술", "안녕나야");

        //when
        Schedule schedule = calenderService.saveSchedule(
                dayScheduleRequestDTO.getName(),
                dayScheduleRequestDTO.getDates(),
                dayScheduleRequestDTO.getContent(),
                dayScheduleRequestDTO.getType());

        em.flush();
        calenderService.deleteSchedule(schedule.getId());
        Optional<Schedule> schedule1 = calenderService.findSchedule(schedule.getId());

        //then
        Assertions.assertEquals(schedule1.isPresent(), false);
    }
}