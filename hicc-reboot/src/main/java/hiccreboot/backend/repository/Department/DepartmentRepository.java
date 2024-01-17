package hiccreboot.backend.repository.Department;

import org.springframework.data.jpa.repository.JpaRepository;

import hiccreboot.backend.domain.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
