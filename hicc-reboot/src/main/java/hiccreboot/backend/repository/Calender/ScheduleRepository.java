package hiccreboot.backend.repository.Calender;

import org.springframework.data.jpa.repository.JpaRepository;

import hiccreboot.backend.domain.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
