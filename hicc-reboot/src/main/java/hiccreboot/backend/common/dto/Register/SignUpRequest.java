package hiccreboot.backend.common.dto.Register;

import hiccreboot.backend.domain.Department;

public record SignUpRequest(String studentNumber, String password, String name, Department department,
							String phoneNumber) {
}
