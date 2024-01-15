package hiccreboot.backend.repository.Calender;

import hiccreboot.backend.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
