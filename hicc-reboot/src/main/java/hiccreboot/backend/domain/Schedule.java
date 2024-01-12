package hiccreboot.backend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ScheduleType scheduleType;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduleDate> scheduleDates = new ArrayList<>();

    @Builder
    private Schedule(String name, String content, ScheduleType scheduleType) {
        this.name = name;
        this.content = content;
        this.scheduleType = scheduleType;
    }

    public static Schedule createSchedule(String name, String content, ScheduleType scheduleType) {
        return Schedule.builder()
                .name(name)
                .content(content)
                .scheduleType(scheduleType)
                .build();
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

