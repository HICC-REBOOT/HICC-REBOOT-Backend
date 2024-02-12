package hiccreboot.backend.domains.auth.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hiccreboot.backend.common.auth.jwt.TokenProvider;
import hiccreboot.backend.common.dto.BaseResponse;
import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.common.exception.dto.ErrorResponse;
import hiccreboot.backend.common.mail.service.EmailService;
import hiccreboot.backend.domains.auth.dto.request.CreateNonceRequest;
import hiccreboot.backend.domains.auth.dto.request.ModifyPasswordRequest;
import hiccreboot.backend.domains.auth.dto.request.SignUpRequest;
import hiccreboot.backend.domains.auth.dto.request.StudentNumberCheckRequest;
import hiccreboot.backend.domains.auth.service.AuthService;
import hiccreboot.backend.domains.department.dto.response.DepartmentResponse;
import hiccreboot.backend.domains.department.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "인증 관련 API", description = "회원가입, 학번 중복 확인, 사용자 정보 등 포함. login, logout 제외")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final DepartmentService departmentService;
	private final AuthService authService;
	private final EmailService emailService;
	private final TokenProvider tokenProvider;

	@PostMapping("/sign-up")
	@Operation(summary = "회원가입")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "성공",
			content = {@Content(schema = @Schema(implementation = BaseResponse.class))}),
		@ApiResponse(responseCode = "500", description = "Internal Server Error")
	})
	public BaseResponse signUp(@Valid @RequestBody SignUpRequest request) {
		authService.signUp(request);
		return DataResponse.ok();
	}

	@PostMapping("/duplicate")
	@Operation(summary = "학번 중복 확인")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "성공",
			content = {@Content(schema = @Schema(implementation = BaseResponse.class))}),
		@ApiResponse(responseCode = "409", description = "Internal Server Error",
			content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})
	})
	public BaseResponse checkDuplicate(@Valid @RequestBody StudentNumberCheckRequest request) {
		return authService.checkDuplicate(request);
	}

	@GetMapping("/info")
	@Operation(summary = "헤더 사용자 정보 조회")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "성공",
			content = {@Content(schema = @Schema(implementation = DataResponse.class))}),
		@ApiResponse(responseCode = "500", description = "Internal Server Error",
			content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})
	})
	public BaseResponse userInfo(HttpServletRequest servletRequest) {
		String studentNumber = tokenProvider.extractStudentNumber(servletRequest)
			.orElse(null);
		return authService.findSimpleInfo(studentNumber);
	}

	@GetMapping("/departments")
	public DataResponse<List<DepartmentResponse>> findDepartments() {
		return departmentService.findDepartments();
	}

	@PostMapping("/password/{student-number}")
	public BaseResponse sendNonce(@PathVariable(value = "student-number") String studentNumber,
		@Valid @RequestBody CreateNonceRequest request) {
		emailService.sendNonce(studentNumber, request.getEmail());

		return DataResponse.ok();
	}

	@PatchMapping("/password/verify/{nonce}")
	public BaseResponse verifyNonce(@PathVariable(value = "nonce") String nonce,
		@Valid @RequestBody ModifyPasswordRequest request) {
		authService.resetPassword(nonce, request.getPassword());

		return DataResponse.ok();
	}
}
