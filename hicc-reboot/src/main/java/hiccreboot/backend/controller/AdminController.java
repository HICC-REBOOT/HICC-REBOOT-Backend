package hiccreboot.backend.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hiccreboot.backend.common.dto.BaseResponse;
import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.dto.request.ModifyGradeRequest;
import hiccreboot.backend.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

	private final MemberService memberService;

	@GetMapping("/applicants")
	public BaseResponse findApplicants(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size) {
		return memberService.findAllApplicant(page, size);
	}

	@PatchMapping("/applicants/{applicant-id}")
	public BaseResponse approveApplicant(@PathVariable(value = "applicant-id") Long applicantId) {
		memberService.approve(applicantId);

		return DataResponse.ok();
	}

	@DeleteMapping("/applicants/{applicant-id}")
	public BaseResponse rejectApplicant(@PathVariable(value = "applicant-id") Long applicantId) {
		memberService.reject(applicantId);

		return DataResponse.noContent();
	}

	@GetMapping("/members")
	public BaseResponse findMembers(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size,
		@RequestParam(value = "sort", defaultValue = "grade") String sortBy,
		@RequestParam(value = "search", defaultValue = "", required = false) String searchName) {
		return memberService.findMembers(page, size, sortBy, searchName);
	}

	@PatchMapping("/president/members/{member-id}")
	public BaseResponse modifyGrade(@PathVariable(value = "member-id") Long memberId,
		@Valid @RequestBody ModifyGradeRequest request) {
		memberService.modifyGrade(memberId, request.getGrade());

		return DataResponse.ok();
	}

	@DeleteMapping("/president/members/{member-id}")
	public BaseResponse expel(@PathVariable(value = "member-id") Long memberId) {
		memberService.expel(memberId);

		return DataResponse.noContent();
	}

}
