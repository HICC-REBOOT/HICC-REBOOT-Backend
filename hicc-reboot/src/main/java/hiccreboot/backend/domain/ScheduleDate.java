package hiccreboot.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
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

	@Builder(access = AccessLevel.PRIVATE)
	private ScheduleDate(int year, int month, int dayOfMonth) {
		this.year = year;
		this.month = month;
		this.dayOfMonth = dayOfMonth;
	}

	public static ScheduleDate createScheduleDate(int year, int month, int dayOfMonth) {
		return ScheduleDate.builder()
			.year(year)
			.month(month)
			.dayOfMonth(dayOfMonth)
			.build();
	}

	//연관관계 메소드
	public void addSchedule(Schedule schedule) {
		this.schedule = schedule;
		schedule.getScheduleDates().add(this);
	}
}
