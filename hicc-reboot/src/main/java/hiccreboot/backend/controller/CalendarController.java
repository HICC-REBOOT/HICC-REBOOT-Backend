package hiccreboot.backend.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hiccreboot.backend.common.dto.BaseResponse;
import hiccreboot.backend.common.dto.Calendar.PostScheduleRequest;
import hiccreboot.backend.common.dto.Calendar.UpdateScheduleRequest;
import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.service.CalendarService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class CalendarController {

	private final CalendarService calendarService;

	@GetMapping("/month-schedule")
	public BaseResponse searchMonthSchedule(@RequestParam("year") int year, @RequestParam("month") int month) {
		return calendarService.makeMonthSchedules(year, month);
	}

	@GetMapping("/schedule")
	public BaseResponse searchSchedule(@RequestParam("schedule-id") Long id) {
		return calendarService.makeSchedule(id);
	}

	@PostMapping("/schedule")
	public BaseResponse addSchedule(PostScheduleRequest postScheduleRequest) {
		calendarService.saveSchedule(postScheduleRequest.getName(), postScheduleRequest.getDates(),
			postScheduleRequest.getContent(), postScheduleRequest.getType());

		return DataResponse.noContent();
	}

	@DeleteMapping("schedule")
	public BaseResponse deleteSchedule(@RequestParam("schedule-id") Long id) {
		calendarService.deleteSchedule(id);

		return DataResponse.noContent();
	}

	@PatchMapping("schedule")
	public BaseResponse updateSchedule(UpdateScheduleRequest updateScheduleRequest) {
		// 이 부분 수정
		calendarService.updateSchedule(updateScheduleRequest);

		return DataResponse.noContent();
	}
}
