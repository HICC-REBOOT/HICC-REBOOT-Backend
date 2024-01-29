package hiccreboot.backend.controller;

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
import hiccreboot.backend.common.dto.Calendar.PostScheduleRequest;
import hiccreboot.backend.common.dto.Calendar.UpdateScheduleRequest;
import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.service.CalendarService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class CalendarController {

	private final CalendarService calendarService;
	private final TokenProvider tokenProvider;

	@GetMapping("/month-schedule")
	public BaseResponse searchMonthSchedule(@RequestParam("year") int year, @RequestParam("month") int month) {
		return calendarService.makeMonthSchedules(year, month);
	}

	@GetMapping("/schedule/{schedule-id}")
	public BaseResponse searchSchedule(@PathVariable("schedule-id") Long id) {
		return calendarService.makeSchedule(id);
	}

	@PostMapping("/schedule")
	public BaseResponse addSchedule(@RequestBody PostScheduleRequest postScheduleRequest,
		HttpServletRequest httpServletRequest) {
		String studentNumber = tokenProvider.extractStudentNumber(httpServletRequest).orElse(null);

		calendarService.saveSchedule(studentNumber, postScheduleRequest);

		return DataResponse.noContent();
	}

	@DeleteMapping("schedule/{schedule-id}")
	public BaseResponse deleteSchedule(@PathVariable("schedule-id") Long id, HttpServletRequest httpServletRequest) {
		String studentNumber = tokenProvider.extractStudentNumber(httpServletRequest).orElse(null);

		calendarService.deleteSchedule(studentNumber, id);

		return DataResponse.noContent();
	}

	@PatchMapping("schedule/{schedule-id}")
	public BaseResponse updateSchedule(@PathVariable("schedule-id") Long id,
		@RequestBody UpdateScheduleRequest updateScheduleRequest, HttpServletRequest httpServletRequest) {
		String studentNumber = tokenProvider.extractStudentNumber(httpServletRequest).orElse(null);

		calendarService.updateSchedule(studentNumber, id, updateScheduleRequest);

		return DataResponse.noContent();
	}
}
