package hiccreboot.backend.domains.department.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hiccreboot.backend.domains.department.domain.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
	Optional<Department> findByName(String name);
}
