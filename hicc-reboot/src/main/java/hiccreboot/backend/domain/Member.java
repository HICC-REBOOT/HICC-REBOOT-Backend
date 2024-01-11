package hiccreboot.backend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String studentNumber;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Grade grade;

    @Column(nullable = false)
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPARTMENT_ID")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COLLEGE_ID")
    private College college;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Article> articles = new ArrayList<>();

    private String refreshToken;

    @Builder
    private Member(String studentNumber, Department department, String name, String password, Grade grade, String phoneNumber) {
        this.studentNumber = studentNumber;
        this.department = department;
        this.name = name;
        this.password = password;
        this.grade = grade;
        this.phoneNumber = phoneNumber;
    }

    public static Member createMember(String studentNumber, Department department, String name, String password, Grade grade,
                                      String phoneNumber) {
        return Member.builder()
                .studentNumber(studentNumber)
                .department(department)
                .name(name)
                .password(password)
                .grade(grade)
                .phoneNumber(phoneNumber)
                .build();
    }

    public void updateGrade(Grade grade) {
        this.grade = grade;
    }

    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }

    public void destroyRefreshToken() {
        this.refreshToken = null;
    }

    //연관 관계 메서드
    public void changeDepartment(Department department) {
        this.department = department;
        department.getMembers().add(this);
    }

    public void changeCollege(College college) {
        this.college = college;
        college.getMembers().add(this);
    }


}
