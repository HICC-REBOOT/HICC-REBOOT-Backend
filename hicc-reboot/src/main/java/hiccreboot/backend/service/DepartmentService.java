package hiccreboot.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.dto.response.DepartmentResponse;
import hiccreboot.backend.repository.department.DepartmentRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepartmentService {

	private final DepartmentRepository departmentRepository;

	public DataResponse<List<DepartmentResponse>> findDepartments() {
		List<DepartmentResponse> result = departmentRepository.findAll()
			.stream()
			.map(DepartmentResponse::create).toList();

		return DataResponse.ok(result);
	}
}
