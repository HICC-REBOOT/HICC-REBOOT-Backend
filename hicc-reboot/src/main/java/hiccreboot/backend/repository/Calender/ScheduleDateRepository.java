package hiccreboot.backend.repository.Calender;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hiccreboot.backend.domain.ScheduleDate;

public interface ScheduleDateRepository extends JpaRepository<ScheduleDate, Long> {
	List<ScheduleDate> findAllByYearAndMonth(int year, int month);

	List<ScheduleDate> findAllByYearAndMonthAndDayOfMonth(int year, int month, int day);
}
