package hiccreboot.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hiccreboot.backend.common.auth.jwt.TokenProvider;
import hiccreboot.backend.common.dto.BaseResponse;
import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.dto.request.SignUpRequest;
import hiccreboot.backend.dto.request.StudentNumberCheckRequest;
import hiccreboot.backend.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final MemberService memberService;
	private final TokenProvider tokenProvider;

	@PostMapping("/sign-up")
	public BaseResponse signUp(@Valid @RequestBody SignUpRequest request) {
		memberService.signUp(request);
		return DataResponse.ok();
	}

	@PostMapping("/duplicate")
	public BaseResponse checkDuplicate(@Valid @RequestBody StudentNumberCheckRequest request) {
		return memberService.checkDuplicate(request);
	}

	@GetMapping("/info")
	public BaseResponse userInfo(HttpServletRequest servletRequest) {
		String studentNumber = tokenProvider.extractStudentNumber(servletRequest)
			.orElse(null);
		return memberService.findSimpleInfo(studentNumber);
	}
}
