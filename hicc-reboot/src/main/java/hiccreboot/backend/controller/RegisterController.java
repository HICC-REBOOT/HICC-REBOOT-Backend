package hiccreboot.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hiccreboot.backend.common.dto.Register.SignUpRequest;
import hiccreboot.backend.service.DepartmentService;
import hiccreboot.backend.service.RegisterService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/register")
@RequiredArgsConstructor
public class RegisterController {

	private final MemberService memberService;
	private final DepartmentService departmentService;
	private final RegisterService registerService;

	@PostMapping("/student-number")
	public Object isDuplicatedStudentNumber(@RequestParam("studentNumber") String studentNumber) {
		return registerService.isDuplicatedId(studentNumber);
	}

	@GetMapping("/department")
	public Object searchDepartment() {
		departmentService.findAllDepartments();

		// 이 부분 리턴에 맞게 수정
	}

	@PostMapping("/sign-up")
	public Object signUp(@RequestBody SignUpRequest signUpRequest) {
		memberService.saveMember(
			signUpRequest.studentNumber(),
			signUpRequest.password(),
			signUpRequest.name(),
			signUpRequest.department(),
			signUpRequest.phoneNumber());
	}
}
