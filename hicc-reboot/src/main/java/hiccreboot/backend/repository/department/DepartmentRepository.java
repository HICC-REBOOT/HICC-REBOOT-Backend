package hiccreboot.backend.repository.department;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hiccreboot.backend.domain.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
	Optional<Department> findByName(String name);
}
