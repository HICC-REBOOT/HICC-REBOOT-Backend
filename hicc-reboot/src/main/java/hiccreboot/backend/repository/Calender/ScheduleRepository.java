package hiccreboot.backend.repository.Calender;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hiccreboot.backend.domain.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
	@Query("select distinct s from Schedule s JOIN s.scheduleDates sd on sd.year = :year and sd.month = :month")
	List<Schedule> findAllByYearAndMonth(int year, int month);

}
