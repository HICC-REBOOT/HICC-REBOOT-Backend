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
public class College {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COLLEGE_ID")
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "DEPARTMENT_ID")
    private Department department;

    @OneToMany
    private List<Member> members = new ArrayList<>();

    @Builder
    private College(String name, Department department) {
        this.name = name;
        changeDepartment(department);
    }

    public College createCollege(String name, Department department) {
        return College.builder()
                .name(name)
                .department(department)
                .build();
    }

    //연관 관계 메서드
    public void changeDepartment(Department department) {
        this.department = department;
        department.getCollege().add(this);
    }
}
