package hiccreboot.backend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(uniqueConstraints = {@UniqueConstraint(name = "UniqueDepartment", columnNames = {"name"})})
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DEPARTMENT_ID")
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "department")
    private List<College> college = new ArrayList<>();

    @OneToMany
    private List<Member> members = new ArrayList<>();

    private Department(String name) {
        this.name = name;
    }

    public Department createDepartment(String name) {
        return new Department(name);
    }
}
