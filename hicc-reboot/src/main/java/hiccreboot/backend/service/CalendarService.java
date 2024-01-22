package hiccreboot.backend.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hiccreboot.backend.common.dto.BaseResponse;
import hiccreboot.backend.common.dto.Calendar.ScheduleResponse;
import hiccreboot.backend.common.dto.Calendar.SimpleScheduleResponse;
import hiccreboot.backend.common.dto.Calendar.UpdateScheduleRequest;
import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.common.exception.ScheduleNotFoundException;
import hiccreboot.backend.domain.Schedule;
import hiccreboot.backend.domain.ScheduleDate;
import hiccreboot.backend.domain.ScheduleType;
import hiccreboot.backend.repository.Calender.ScheduleDateRepository;
import hiccreboot.backend.repository.Calender.ScheduleRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalendarService {

	private final ScheduleRepository scheduleRepository;
	private final ScheduleDateRepository scheduleDateRepository;

	private List<ScheduleDate> findScheduleDatesByMonth(int year, int month) {
		return scheduleDateRepository.findAllByYearAndMonth(year, month);
	}

	public BaseResponse makeMonthSchedules(int year, int month) {
		List<ScheduleDate> scheduleDates = findScheduleDatesByMonth(year, month);

		List<SimpleScheduleResponse> simpleScheduleResponses = new ArrayList<>();
		scheduleDates.stream().forEach(scheduleDate -> {
			simpleScheduleResponses.add(
				SimpleScheduleResponse.create(scheduleDate));
		});

		return DataResponse.ok(simpleScheduleResponses);
	}

	public Optional<Schedule> findSchedule(Long id) {
		return scheduleRepository.findById(id);
	}

	public BaseResponse makeSchedule(Long id) {
		Schedule schedule = findSchedule(id).orElseThrow(() -> ScheduleNotFoundException.EXCEPTION);

		List<LocalDate> localDates = new ArrayList<>();
		schedule.getScheduleDates()
			.stream()
			.forEach(scheduleDate -> localDates.add(
				LocalDate.of(scheduleDate.getYear(), scheduleDate.getMonth(), scheduleDate.getDayOfMonth())));

		return DataResponse.ok(
			new ScheduleResponse(schedule.getName(), schedule.getId(), localDates, schedule.getScheduleType(),
				schedule.getContent()));
	}

	@Transactional
	public Schedule saveSchedule(String name, List<LocalDate> dates, String content, ScheduleType type) {
		Schedule schedule = Schedule.createSchedule(name, content, type);

		dates.stream()
			.forEach(date -> {
				ScheduleDate.createScheduleDate(date.getYear(), date.getMonthValue(),
					date.getDayOfMonth(), schedule);
			});
		scheduleRepository.save(schedule);

		return schedule;
	}

	@Transactional
	public void deleteSchedule(Long id) {
		scheduleRepository.deleteById(id);
	}

	@Transactional
	public Schedule updateSchedule(Long id, UpdateScheduleRequest updateScheduleRequest) {
		Schedule schedule = findSchedule(id).orElseThrow(() -> ScheduleNotFoundException.EXCEPTION);

		//scheduleDate 삭제
		schedule.getScheduleDates().forEach(scheduleDate -> scheduleDateRepository.deleteById(scheduleDate.getId()));
		schedule.getScheduleDates().clear();

		//schedule 변경
		schedule.updateName(updateScheduleRequest.getName());
		schedule.updateContent(updateScheduleRequest.getContent());
		schedule.updateScheduleType(updateScheduleRequest.getType());
		updateScheduleRequest.getDates().stream()
			.forEach(date -> {
				ScheduleDate.createScheduleDate(date.getYear(), date.getMonthValue(),
					date.getDayOfMonth(), schedule);
			});

		return schedule;
	}
}
