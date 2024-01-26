package hiccreboot.backend.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hiccreboot.backend.common.auth.jwt.TokenProvider;
import hiccreboot.backend.common.dto.BaseResponse;
import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.dto.request.ProfileModifyRequest;
import hiccreboot.backend.dto.response.PersonalArticleResponse;
import hiccreboot.backend.dto.response.PersonalCommentResponse;
import hiccreboot.backend.dto.response.ProfileMemberResponse;
import hiccreboot.backend.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

	private final MemberService memberService;
	private final TokenProvider tokenProvider;

	@GetMapping
	public DataResponse<ProfileMemberResponse> getProfile(HttpServletRequest servletRequest) {
		String studentNumber = tokenProvider.extractStudentNumber(servletRequest).get();

		return memberService.getProfile(studentNumber);
	}

	@PatchMapping
	public BaseResponse modifyProfile(@Valid @RequestBody ProfileModifyRequest request,
		HttpServletRequest servletRequest) {
		String studentNumber = tokenProvider.extractStudentNumber(servletRequest).get();
		memberService.modifyProfile(request, studentNumber);

		return DataResponse.ok();
	}

	@DeleteMapping
	public BaseResponse withdraw(HttpServletRequest servletRequest) {
		String studentNumber = tokenProvider.extractStudentNumber(servletRequest).get();

		memberService.withdraw(studentNumber);

		return DataResponse.noContent();
	}

	@GetMapping("/articles")
	public DataResponse<Page<PersonalArticleResponse>> findPersonalArticles(@RequestParam(value = "page") int page,
		@RequestParam(value = "size") int size, HttpServletRequest servletRequest) {
		String studentNumber = tokenProvider.extractStudentNumber(servletRequest).get();

		return memberService.findPersonalArticles(page, size, studentNumber);
	}

	@GetMapping("/comments")
	public DataResponse<Page<PersonalCommentResponse>> findPersonalComments(@RequestParam(value = "page") int page,
		@RequestParam(value = "size") int size, HttpServletRequest servletRequest) {
		String studentNumber = tokenProvider.extractStudentNumber(servletRequest).get();

		return memberService.findPersonalComments(page, size, studentNumber);
	}
}
