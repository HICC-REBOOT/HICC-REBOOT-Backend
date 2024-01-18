package hiccreboot.backend.common.exception;

import static hiccreboot.backend.common.consts.HttpConstants.*;

import hiccreboot.backend.common.exception.dto.ErrorReason;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum GlobalErrorCode implements BaseErrorCode {
	MEMBER_NOT_FOUND(NOT_FOUND, "USER_NOT_FOUND_404_1", "회원을 찾을 수 없습니다."),
	ARTICLE_NOT_FOUND(NOT_FOUND, "ARTICLE_NOT_FOUND_404_1", "글을 찾을 수 없습니다."),
	SCHEDULE_NOT_FOUND(NOT_FOUND, "SCHEDULE_FOUND_404_1", "스케쥴을 찾을 수 없습니다."),
	COMMENT_NOT_FOUND(NOT_FOUND, "COMMENT_FOUND_404_1", "댓글을 찾을 수 없습니다."),
	EMAIL_DUPLICATE(CONFLICT, "EMAIL_DUPLICATE_409_1", "이미 존재하는 이메일입니다."),
	STUDENT_NUMBER_DUPLICATE(CONFLICT, "STUDENT_NUMBER_DUPLICATE_409_1", "이미 존재하는 회원입니다.");
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
		return null;
	}
}
