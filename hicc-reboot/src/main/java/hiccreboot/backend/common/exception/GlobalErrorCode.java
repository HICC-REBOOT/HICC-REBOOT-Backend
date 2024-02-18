package hiccreboot.backend.common.exception;

import static hiccreboot.backend.common.consts.HttpConstants.*;

import hiccreboot.backend.common.exception.dto.ErrorReason;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum GlobalErrorCode implements BaseErrorCode {

	// 401
	ACCESS_TOKEN_UNAUTHORIZED(UNAUTHORIZED, "ACCESS_TOKEN_UNAUTHORIZED_401_1", "액세스 토큰 인증을 실패하였습니다."),
	REFRESH_TOKEN_UNAUTHORIZED(UNAUTHORIZED, "REFRESH_TOKEN_UNAUTHORIZED_401_1", "리프레쉬 토큰 인증을 실패하였습니다."),
	// 403
	ACCESS_FORBIDDEN(FORBIDDEN, "FORBIDDEN_403_1", "접근 권한이 없습니다."),
	ACCESS_DENIED(FORBIDDEN, "FORBIDDEN_403_2", "접근 권한이 없습니다."),
	// 404
	MEMBER_NOT_FOUND(NOT_FOUND, "MEMBER_NOT_FOUND_404_1", "회원을 찾을 수 없습니다."),
	ARTICLE_NOT_FOUND(NOT_FOUND, "ARTICLE_NOT_FOUND_404_1", "글을 찾을 수 없습니다."),
	SCHEDULE_NOT_FOUND(NOT_FOUND, "SCHEDULE_FOUND_404_1", "스케쥴을 찾을 수 없습니다."),
	COMMENT_NOT_FOUND(NOT_FOUND, "COMMENT_FOUND_404_1", "댓글을 찾을 수 없습니다."),
	DEPARTMENT_NOT_FOUND(NOT_FOUND, "DEPARTMENT_NOT_FOUND_404_1", "학과를 찾을 수 없습니다."),
	PASSWORD_RESET_NOT_FOUND(NOT_FOUND, "PASSWORD_RESET_NOT_FOUND_404_1", "비밀번호 인증 요청을 찾을 수 없습니다."),
	SORT_NOT_FOUND(NOT_FOUND, " SORT_NOT_FOUND_404_1", "분류기준을 찾을 수 없습니다."),
	FILE_NAME_EXTENSION_NOT_FOUND(NOT_FOUND, " FILE_NAME_EXTENSION_NOT_FOUND_404_1", "파일확장자를 찾을 수 없습니다."),
	// 409
	EMAIL_DUPLICATE(CONFLICT, "EMAIL_DUPLICATE_409_1", "이미 존재하는 이메일입니다."),
	STUDENT_NUMBER_DUPLICATE(CONFLICT, "STUDENT_NUMBER_DUPLICATE_409_1", "이미 존재하는 회원입니다."),
	EMAIL_MISMATCH(CONFLICT, "EMAIL_MISMATCH_409_1", "이메일이 일치하지 않습니다."),
	// 412
	DATETIME_PRECONDITION_FAILED(PRECONDITION_FAILED, "DATETIME_PRECONDITION_FAILED_412_1", "DateTime이 형식에 맞지 않습니다."),
	// 413
	IMAGE_COUNT_TOO_LARGE(PAYLOAD_TOO_LARGE, "IMAGE_COUNT_TOO_LARGE_413_1", "이미지 개수가 너무 많습니다."),
	// 500
	INTERNAL_SERVER_EXCEPTION(INTERNAL_SERVER, "INTERNAL_SERVER_ERROR_500_1", "내부 서버 에러입니다."),
	;

	private final Integer status;
	private final String code;
	private final String reason;

	@Override
	public ErrorReason getErrorReason() {
		return ErrorReason.builder()
			.status(status)
			.code(code)
			.reason(reason)
			.build();
	}

	@Override
	public String getExplainError() throws NoSuchFieldException {
		return reason;
	}
}
