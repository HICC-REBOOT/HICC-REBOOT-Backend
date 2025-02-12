package hiccreboot.backend.domains.calendar.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hiccreboot.backend.common.auth.jwt.TokenProvider;
import hiccreboot.backend.common.dto.BaseResponse;
import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.domains.calendar.dto.request.PostScheduleRequest;
import hiccreboot.backend.domains.calendar.dto.request.UpdateScheduleRequest;
import hiccreboot.backend.domains.calendar.dto.response.ScheduleDateResponse;
import hiccreboot.backend.domains.calendar.dto.response.ScheduleResponse;
import hiccreboot.backend.domains.calendar.service.CalendarService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class CalendarController {

	private final CalendarService calendarService;
	private final TokenProvider tokenProvider;

	@GetMapping("/month-schedule")
	@Operation(summary = "월별 일정 조회", description = "해당 월의 일정을 조회하는 api")
	public DataResponse<List<ScheduleResponse>> searchMonthSchedule(@RequestParam("year") int year,
		@RequestParam("month") int month) {
		return calendarService.makeMonthSchedules(year, month);
	}

	@GetMapping("/date-schedule")
	@Operation(summary = "날짜별 일정 조회", description = "해당 날짜의 일정을 조회하는 api")
	public DataResponse<List<ScheduleDateResponse>> findScheduleByDate(@RequestParam("year") int year,
		@RequestParam("month") int month, @RequestParam("day") int day) {
		return calendarService.findScheduleByDate(year, month, day);
	}

	@GetMapping("/schedule/{schedule-id}")
	@Operation(summary = "일정 상세 조회", description = "일정id로 상세를 조회하는 api")
	public DataResponse<ScheduleResponse> searchSchedule(@PathVariable("schedule-id") Long id) {
		return calendarService.makeSchedule(id);
	}

	@PostMapping("/schedule")
	@Operation(summary = "일정 추가", description = "일정을 추가하는 api")
	public BaseResponse addSchedule(@Valid @RequestBody PostScheduleRequest postScheduleRequest,
		HttpServletRequest httpServletRequest) {
		String studentNumber = tokenProvider.extractStudentNumber(httpServletRequest).orElse(null);

		calendarService.saveSchedule(studentNumber, postScheduleRequest);

		return DataResponse.noContent();
	}

	@DeleteMapping("schedule/{schedule-id}")
	@Operation(summary = "일정 삭제", description = "일정을 삭제하는 api")
	public BaseResponse deleteSchedule(@PathVariable("schedule-id") Long id, HttpServletRequest httpServletRequest) {
		String studentNumber = tokenProvider.extractStudentNumber(httpServletRequest).orElse(null);

		calendarService.deleteSchedule(studentNumber, id);

		return DataResponse.noContent();
	}

	@PatchMapping("schedule/{schedule-id}")
	@Operation(summary = "일정 수정", description = "일정을 수정하는 api")
	public BaseResponse updateSchedule(@PathVariable("schedule-id") Long id,
		@Valid @RequestBody UpdateScheduleRequest updateScheduleRequest, HttpServletRequest httpServletRequest) {
		String studentNumber = tokenProvider.extractStudentNumber(httpServletRequest).orElse(null);

		calendarService.updateSchedule(studentNumber, id, updateScheduleRequest);

		return DataResponse.noContent();
	}
}
