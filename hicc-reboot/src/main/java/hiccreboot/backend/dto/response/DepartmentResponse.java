package hiccreboot.backend.dto.response;

import hiccreboot.backend.domain.Department;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class DepartmentResponse {
	private String name;

	public static DepartmentResponse create(Department department) {
		return DepartmentResponse.builder()
			.name(department.getName())
			.build();
	}
}
