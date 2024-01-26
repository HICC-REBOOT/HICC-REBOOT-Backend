package hiccreboot.backend.repository.Calender;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hiccreboot.backend.domain.ScheduleDate;

public interface ScheduleDateRepository extends JpaRepository<ScheduleDate, Long> {
	public List<ScheduleDate> findAllByYearAndMonth(int year, int month);
}
