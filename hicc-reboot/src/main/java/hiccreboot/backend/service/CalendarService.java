package hiccreboot.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hiccreboot.backend.common.dto.BaseResponse;
import hiccreboot.backend.common.dto.Calendar.PostScheduleRequest;
import hiccreboot.backend.common.dto.Calendar.ScheduleDateResponse;
import hiccreboot.backend.common.dto.Calendar.ScheduleResponse;
import hiccreboot.backend.common.dto.Calendar.SimpleScheduleResponse;
import hiccreboot.backend.common.dto.Calendar.UpdateScheduleRequest;
import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.common.exception.AccessForbiddenException;
import hiccreboot.backend.common.exception.MemberNotFoundException;
import hiccreboot.backend.common.exception.ScheduleNotFoundException;
import hiccreboot.backend.domain.Grade;
import hiccreboot.backend.domain.Member;
import hiccreboot.backend.domain.Schedule;
import hiccreboot.backend.domain.ScheduleDate;
import hiccreboot.backend.repository.Calender.ScheduleDateRepository;
import hiccreboot.backend.repository.Calender.ScheduleRepository;
import hiccreboot.backend.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalendarService {

	private final ScheduleRepository scheduleRepository;
	private final ScheduleDateRepository scheduleDateRepository;
	private final MemberRepository memberRepository;

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

	public DataResponse<List<ScheduleDateResponse>> findScheduleByDate(int year, int month, int day) {
		List<ScheduleDateResponse> result = scheduleDateRepository.findAllByYearAndMonthAndDayOfMonth(year, month, day)
			.stream()
			.map(ScheduleDateResponse::create)
			.toList();

		return DataResponse.ok(result);
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
	public Schedule saveSchedule(String studentNumber, PostScheduleRequest postScheduleRequest) {
		Member member = memberRepository.findByStudentNumber(studentNumber)
			.orElseThrow(() -> MemberNotFoundException.EXCEPTION);

		checkCalendarAuthority(member.getGrade());

		Schedule schedule = Schedule.createSchedule(postScheduleRequest);

		List<LocalDate> dates = postScheduleRequest.getDates();
		dates.stream()
			.forEach(date -> ScheduleDate.create(date, schedule));
		scheduleRepository.save(schedule);

		return schedule;
	}

	@Transactional
	public void deleteSchedule(String studentNumber, Long id) {
		Member member = memberRepository.findByStudentNumber(studentNumber)
			.orElseThrow(() -> MemberNotFoundException.EXCEPTION);

		checkCalendarAuthority(member.getGrade());

		scheduleRepository.deleteById(id);
	}

	@Transactional
	public void updateSchedule(String studentNumber, Long id, UpdateScheduleRequest updateScheduleRequest) {
		Member member = memberRepository.findByStudentNumber(studentNumber)
			.orElseThrow(() -> MemberNotFoundException.EXCEPTION);

		checkCalendarAuthority(member.getGrade());

		Schedule schedule = findSchedule(id).orElseThrow(() -> ScheduleNotFoundException.EXCEPTION);

		//schedule 변경
		schedule.updateName(updateScheduleRequest.getName());
		schedule.updateContent(updateScheduleRequest.getContent());
		schedule.updateScheduleType(updateScheduleRequest.getType());

		schedule.getScheduleDates().clear();
		updateScheduleRequest.getDates()
			.forEach(date -> ScheduleDate.create(date, schedule));
	}

	private void checkCalendarAuthority(Grade grade) {
		if (grade == Grade.EXECUTIVE || grade == Grade.PRESIDENT) {
			return;
		}
		throw AccessForbiddenException.EXCEPTION;
	}
}
