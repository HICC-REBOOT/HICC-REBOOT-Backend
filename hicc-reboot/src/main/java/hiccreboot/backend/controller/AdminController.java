package hiccreboot.backend.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hiccreboot.backend.common.dto.BaseResponse;
import hiccreboot.backend.service.MemberService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

	private final MemberService memberService;

	@GetMapping("/applicants")
	public BaseResponse getApplicants(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size) {
		return memberService.findAllApplicant(page, size);
	}

	@PatchMapping("/applicants/{applicant-id}")
	public BaseResponse approveApplicant(@PathVariable(value = "applicant-id") Long applicantId) {
		return memberService.approve(applicantId);
	}

	@DeleteMapping("/applicants/{applicant-id}")
	public BaseResponse rejectApplicant(@PathVariable(value = "applicant-id") Long applicantId) {
		return memberService.reject(applicantId);
	}

}
