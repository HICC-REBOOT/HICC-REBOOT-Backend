package hiccreboot.backend.domains.member.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hiccreboot.backend.common.auth.jwt.TokenProvider;
import hiccreboot.backend.common.dto.BaseResponse;
import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.domains.member.dto.request.ModifyGradeRequest;
import hiccreboot.backend.domains.member.dto.response.ApplicantResponse;
import hiccreboot.backend.domains.member.dto.response.MemberResponse;
import hiccreboot.backend.domains.member.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

	private final TokenProvider tokenProvider;
	private final AdminService adminService;

	@GetMapping("/applicants")
	@Operation(summary = "지원자 조회", description = "지원자를 조회하는 api")
	public DataResponse<Page<ApplicantResponse>> findApplicants(@RequestParam(value = "page") int page,
		@RequestParam(value = "size") int size) {
		return adminService.findAllApplicant(page, size);
	}

	@PatchMapping("/applicants/{applicant-id}")
	@Operation(summary = "지원자 승인", description = "지원자를 승인하는 api")
	public BaseResponse approveApplicant(@PathVariable(value = "applicant-id") Long applicantId) {
		adminService.approve(applicantId);

		return DataResponse.ok();
	}

	@DeleteMapping("/applicants/{applicant-id}")
	@Operation(summary = "지원자 거절", description = "지원자를 거절하는 api")
	public BaseResponse rejectApplicant(@PathVariable(value = "applicant-id") Long applicantId) {
		adminService.reject(applicantId);

		return DataResponse.noContent();
	}

	@GetMapping("/members")
	@Operation(summary = "회원 목록 조회", description = "회원을 조회하는 api")
	public DataResponse<Page<MemberResponse>> findMembers(@RequestParam(value = "page") int page,
		@RequestParam(value = "size") int size,
		@RequestParam(value = "sort", defaultValue = "grade") String sortBy,
		@RequestParam(value = "search", defaultValue = "", required = false) String searchName) {
		return adminService.findMembers(page, size, sortBy, searchName);
	}

	@GetMapping("/president/members")
	@Operation(summary = "회장이 회원 목록 조회", description = "회장이 회원을 조회하는 api")
	public DataResponse<Page<MemberResponse>> findMembersByPresident(@RequestParam(value = "page") int page,
		@RequestParam(value = "size") int size,
		@RequestParam(value = "sort", defaultValue = "grade") String sortBy,
		@RequestParam(value = "search", defaultValue = "", required = false) String searchName) {
		return adminService.findMembers(page, size, sortBy, searchName);
	}

	@PatchMapping("/president/members/{member-id}")
	@Operation(summary = "회장이 등급 수정", description = "회장이 등급을 수정하는 api")
	public BaseResponse modifyGrade(@PathVariable(value = "member-id") Long memberId,
		@Valid @RequestBody ModifyGradeRequest request, HttpServletRequest servletRequest) {
		String presidentStudentNumber = tokenProvider.extractStudentNumber(servletRequest).get();
		adminService.modifyGrade(memberId, request.getGrade(), presidentStudentNumber);

		return DataResponse.ok();
	}

	@DeleteMapping("/president/members/{member-id}")
	@Operation(summary = "회장이 회원 추방", description = "회장이 회원을 추방하는 api")
	public BaseResponse expel(@PathVariable(value = "member-id") Long memberId, HttpServletRequest servletRequest) {
		String presidentStudentNumber = tokenProvider.extractStudentNumber(servletRequest).get();
		adminService.expel(memberId, presidentStudentNumber);

		return DataResponse.noContent();
	}

}
