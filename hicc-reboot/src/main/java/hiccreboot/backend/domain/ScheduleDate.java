package hiccreboot.backend.domain;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleDate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SCHEDULE_DATE_ID")
	private Long id;

	@Column(nullable = false)
	private int year;

	@Column(nullable = false)
	private int month;

	@Column(nullable = false)
	private int dayOfMonth;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SCHEDULE_ID")
	private Schedule schedule;

	private ScheduleDate(int year, int month, int dayOfMonth, Schedule schedule) {
		this.year = year;
		this.month = month;
		this.dayOfMonth = dayOfMonth;
		addSchedule(schedule);
	}

	public static ScheduleDate create(int year, int month, int dayOfMonth, Schedule schedule) {
		return new ScheduleDate(year, month, dayOfMonth, schedule);
	}

	public static ScheduleDate create(LocalDate localDate, Schedule schedule) {
		return new ScheduleDate(
			localDate.getYear(),
			localDate.getMonthValue(),
			localDate.getDayOfMonth(),
			schedule
		);
	}

	//연관관계 메소드
	private void addSchedule(Schedule schedule) {
		this.schedule = schedule;
		schedule.getScheduleDates().add(this);
	}
}
