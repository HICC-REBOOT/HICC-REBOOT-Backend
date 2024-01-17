package hiccreboot.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import hiccreboot.backend.domain.Department;
import hiccreboot.backend.repository.Department.DepartmentRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepartmentService {

	private final DepartmentRepository departmentRepository;

	public List<Department> findAllDepartments() {
		return departmentRepository.findAll();
	}
}
