package hiccreboot.backend.domains.calendar.domain;

import hiccreboot.backend.domains.calendar.dto.request.PostScheduleRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column(nullable = false, length = 300)
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

    public void updateStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public void updateEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }
}

