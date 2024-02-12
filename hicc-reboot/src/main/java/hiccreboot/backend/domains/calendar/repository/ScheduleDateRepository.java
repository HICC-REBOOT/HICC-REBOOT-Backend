package hiccreboot.backend.domains.calendar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hiccreboot.backend.domains.calendar.domain.ScheduleDate;

public interface ScheduleDateRepository extends JpaRepository<ScheduleDate, Long> {
	List<ScheduleDate> findAllByYearAndMonthAndDayOfMonth(int year, int month, int day);
}
