package hiccreboot.backend.domain;

import jakarta.persistence.*;
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

    @Builder
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

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    //연관관계 메소드
    public void addSchedule(Schedule schedule) {
        this.schedule = schedule;
        schedule.getScheduleDates().add(this);
        System.out.println("schedule에 scheduleDate가 추가되었습니다");
    }
}
