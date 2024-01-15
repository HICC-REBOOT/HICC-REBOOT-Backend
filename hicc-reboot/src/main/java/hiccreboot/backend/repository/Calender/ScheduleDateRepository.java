package hiccreboot.backend.repository.Calender;

import hiccreboot.backend.domain.ScheduleDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleDateRepository extends JpaRepository<ScheduleDate, Long> {
    public List<ScheduleDate> findAllByYearAndMonth(int year, int month);
}
