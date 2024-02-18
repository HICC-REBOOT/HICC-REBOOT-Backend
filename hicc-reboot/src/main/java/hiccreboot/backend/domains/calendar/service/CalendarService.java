package hiccreboot.backend.domains.calendar.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.common.exception.AccessForbiddenException;
import hiccreboot.backend.common.exception.DateTimePreconditionFailed;
import hiccreboot.backend.common.exception.MemberNotFoundException;
import hiccreboot.backend.common.exception.ScheduleNotFoundException;
import hiccreboot.backend.domains.calendar.domain.Schedule;
import hiccreboot.backend.domains.calendar.domain.ScheduleDate;
import hiccreboot.backend.domains.calendar.dto.request.PostScheduleRequest;
import hiccreboot.backend.domains.calendar.dto.request.UpdateScheduleRequest;
import hiccreboot.backend.domains.calendar.dto.response.ScheduleDateResponse;
import hiccreboot.backend.domains.calendar.dto.response.ScheduleResponse;
import hiccreboot.backend.domains.calendar.repository.ScheduleDateRepository;
import hiccreboot.backend.domains.calendar.repository.ScheduleRepository;
import hiccreboot.backend.domains.member.domain.Grade;
import hiccreboot.backend.domains.member.domain.Member;
import hiccreboot.backend.domains.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalendarService {

	private final ScheduleDateRepository scheduleDateRepository;
	private final ScheduleRepository scheduleRepository;
	private final MemberRepository memberRepository;

	public DataResponse<List<ScheduleResponse>> makeMonthSchedules(int year, int month) {
		List<ScheduleResponse> scheduleResponses = scheduleRepository.findAllByYearAndMonth(year, month)
			.stream()
			.sorted(Comparator.comparing(Schedule::getStartDateTime))
			.map(ScheduleResponse::create)
			.toList();

		return DataResponse.ok(scheduleResponses);
	}

	public DataResponse<List<ScheduleDateResponse>> findScheduleByDate(int year, int month, int day) {
		List<ScheduleDateResponse> result = scheduleDateRepository.findAllByYearAndMonthAndDayOfMonth(year, month, day)
			.stream()
			.sorted(Comparator.comparing(scheduleDate -> scheduleDate.getSchedule().getStartDateTime()))
			.map(ScheduleDateResponse::create)
			.toList();

		return DataResponse.ok(result);
	}

	public DataResponse<ScheduleResponse> makeSchedule(Long id) {
		Schedule schedule = scheduleRepository.findById(id).orElseThrow(() -> ScheduleNotFoundException.EXCEPTION);

		return DataResponse.ok(
			ScheduleResponse.create(schedule));
	}

	@Transactional
	public Schedule saveSchedule(String studentNumber, PostScheduleRequest postScheduleRequest) {
		Member member = memberRepository.findByStudentNumber(studentNumber)
			.orElseThrow(() -> MemberNotFoundException.EXCEPTION);

		checkCalendarAuthority(member.getGrade());
		validateDates(postScheduleRequest.getStartDateTime(), postScheduleRequest.getEndDateTime());

		Schedule schedule = Schedule.createSchedule(postScheduleRequest);

		LocalDate startDate = postScheduleRequest.getStartDateTime().toLocalDate();
		LocalDate endDate = postScheduleRequest.getEndDateTime().toLocalDate();
		startDate.datesUntil(endDate.plusDays(1)).forEach(localDate -> ScheduleDate.create(localDate, schedule));

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
	public Schedule updateSchedule(String studentNumber, Long id, UpdateScheduleRequest updateScheduleRequest) {
		Member member = memberRepository.findByStudentNumber(studentNumber)
			.orElseThrow(() -> MemberNotFoundException.EXCEPTION);

		checkCalendarAuthority(member.getGrade());
		validateDates(updateScheduleRequest.getStartDateTime(), updateScheduleRequest.getEndDateTime());

		Schedule schedule = scheduleRepository.findById(id).orElseThrow(() -> ScheduleNotFoundException.EXCEPTION);

		//schedule 변경
		schedule.updateName(updateScheduleRequest.getName());
		schedule.updateContent(updateScheduleRequest.getContent());
		schedule.updateScheduleType(updateScheduleRequest.getType());
		schedule.updateStartDateTime(updateScheduleRequest.getStartDateTime());
		schedule.updateEndDateTime(updateScheduleRequest.getEndDateTime());

		schedule.getScheduleDates().clear();
		LocalDate startDate = updateScheduleRequest.getStartDateTime().toLocalDate();
		LocalDate endDate = updateScheduleRequest.getEndDateTime().toLocalDate();
		startDate.datesUntil(endDate.plusDays(1)).forEach(localDate -> ScheduleDate.create(localDate, schedule));

		return schedule;
	}

	private void checkCalendarAuthority(Grade grade) {
		if (grade == Grade.EXECUTIVE || grade == Grade.PRESIDENT) {
			return;
		}
		throw AccessForbiddenException.EXCEPTION;
	}

	private void validateDates(LocalDateTime startDateTime, LocalDateTime endDateTime) {
		if (!endDateTime.isBefore(startDateTime)) {
			return;
		}
		throw DateTimePreconditionFailed.EXCEPTION;
	}

}
