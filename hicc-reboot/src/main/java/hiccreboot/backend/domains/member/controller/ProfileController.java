package hiccreboot.backend.domains.member.controller;

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
import hiccreboot.backend.domains.member.dto.request.ProfileModifyRequest;
import hiccreboot.backend.domains.member.dto.response.PersonalArticleResponse;
import hiccreboot.backend.domains.member.dto.response.PersonalCommentResponse;
import hiccreboot.backend.domains.member.dto.response.ProfileMemberResponse;
import hiccreboot.backend.domains.member.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

	private final TokenProvider tokenProvider;
	private final ProfileService profileService;

	@GetMapping
	@Operation(summary = "회원 프로필 조회", description = "프로필을 조회하는 api")
	public DataResponse<ProfileMemberResponse> getProfile(HttpServletRequest servletRequest) {
		String studentNumber = tokenProvider.extractStudentNumber(servletRequest).get();

		return profileService.getProfile(studentNumber);
	}

	@PatchMapping
	@Operation(summary = "회원 프로필 수정", description = "프로필을 수정하는 api")
	public BaseResponse modifyProfile(@Valid @RequestBody ProfileModifyRequest request,
		HttpServletRequest servletRequest) {
		String studentNumber = tokenProvider.extractStudentNumber(servletRequest).get();
		profileService.modifyProfile(request, studentNumber);

		return DataResponse.ok();
	}

	@DeleteMapping
	@Operation(summary = "회원 탈퇴", description = "회원을 탈퇴하는 api")
	public BaseResponse withdraw(HttpServletRequest servletRequest) {
		String studentNumber = tokenProvider.extractStudentNumber(servletRequest).get();

		profileService.withdraw(studentNumber);

		return DataResponse.noContent();
	}

	@GetMapping("/articles")
	@Operation(summary = "개인 게시글 조회", description = "개인 게시글을 조회하는 api")
	public DataResponse<Page<PersonalArticleResponse>> findPersonalArticles(@RequestParam(value = "page") int page,
		@RequestParam(value = "size") int size, HttpServletRequest servletRequest) {
		String studentNumber = tokenProvider.extractStudentNumber(servletRequest).get();

		return profileService.findPersonalArticles(page, size, studentNumber);
	}

	@GetMapping("/comments")
	@Operation(summary = "개인 댓글 조회", description = "개인 댓글을 조회하는 api")
	public DataResponse<Page<PersonalCommentResponse>> findPersonalComments(@RequestParam(value = "page") int page,
		@RequestParam(value = "size") int size, HttpServletRequest servletRequest) {
		String studentNumber = tokenProvider.extractStudentNumber(servletRequest).get();

		return profileService.findPersonalComments(page, size, studentNumber);
	}
}
