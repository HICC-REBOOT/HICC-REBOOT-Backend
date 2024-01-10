package hiccreboot.backend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
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
    private String content;

    @Column(nullable = false)
    @Enumerated
    private ScheduleType scheduleType;

    @OneToMany
    @JoinColumn(name = "SCHEDULE_ID")
    private List<ScheduleDate> scheduleDates = new ArrayList<>();

    //연관관계 메소드
    public void addScheduleDate(ScheduleDate scheduleDate) {
        this.scheduleDates.add(scheduleDate);
        scheduleDate.setSchedule(this);
    }
}

