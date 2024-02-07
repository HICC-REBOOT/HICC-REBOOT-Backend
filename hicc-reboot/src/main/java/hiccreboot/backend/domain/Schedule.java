package hiccreboot.backend.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import hiccreboot.backend.common.dto.Calendar.PostScheduleRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SCHEDULE_ID")
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private LocalDateTime startDateTime;

	@Column(nullable = false)
	private LocalDateTime endDateTime;

	@OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
	List<ScheduleDate> scheduleDates = new ArrayList<>();

	@Column(nullable = false)
	private String content;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private ScheduleType scheduleType;

	private Schedule(String name, LocalDateTime startDateTime, LocalDateTime endDateTime, String content,
		ScheduleType scheduleType) {

		this.name = name;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.content = content;
		this.scheduleType = scheduleType;
	}

	public static Schedule createSchedule(String name, LocalDateTime startDateTime, LocalDateTime endDateTime,
		String content, ScheduleType scheduleType) {
		return new Schedule(name, startDateTime, endDateTime, content, scheduleType);
	}

	public static Schedule createSchedule(PostScheduleRequest postScheduleRequest) {
		return new Schedule(postScheduleRequest.getName(), postScheduleRequest.getStartDateTime(),
			postScheduleRequest.getEndDateTime(), postScheduleRequest.getContent(), postScheduleRequest.getType());
	}

	public void updateName(String name) {
		this.name = name;
	}

	public void updateContent(String content) {
		this.content = content;
	}

	public void updateScheduleType(ScheduleType scheduleType) {
		this.scheduleType = scheduleType;
	}
}

