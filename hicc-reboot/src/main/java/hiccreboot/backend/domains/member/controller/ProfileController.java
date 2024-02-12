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
	public DataResponse<ProfileMemberResponse> getProfile(HttpServletRequest servletRequest) {
		String studentNumber = tokenProvider.extractStudentNumber(servletRequest).get();

		return profileService.getProfile(studentNumber);
	}

	@PatchMapping
	public BaseResponse modifyProfile(@Valid @RequestBody ProfileModifyRequest request,
		HttpServletRequest servletRequest) {
		String studentNumber = tokenProvider.extractStudentNumber(servletRequest).get();
		profileService.modifyProfile(request, studentNumber);

		return DataResponse.ok();
	}

	@DeleteMapping
	public BaseResponse withdraw(HttpServletRequest servletRequest) {
		String studentNumber = tokenProvider.extractStudentNumber(servletRequest).get();

		profileService.withdraw(studentNumber);

		return DataResponse.noContent();
	}

	@GetMapping("/articles")
	public DataResponse<Page<PersonalArticleResponse>> findPersonalArticles(@RequestParam(value = "page") int page,
		@RequestParam(value = "size") int size, HttpServletRequest servletRequest) {
		String studentNumber = tokenProvider.extractStudentNumber(servletRequest).get();

		return profileService.findPersonalArticles(page, size, studentNumber);
	}

	@GetMapping("/comments")
	public DataResponse<Page<PersonalCommentResponse>> findPersonalComments(@RequestParam(value = "page") int page,
		@RequestParam(value = "size") int size, HttpServletRequest servletRequest) {
		String studentNumber = tokenProvider.extractStudentNumber(servletRequest).get();

		return profileService.findPersonalComments(page, size, studentNumber);
	}
}
