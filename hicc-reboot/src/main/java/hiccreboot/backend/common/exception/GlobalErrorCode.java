package hiccreboot.backend.common.exception;

import hiccreboot.backend.common.exception.dto.ErrorReason;
import lombok.AllArgsConstructor;

import static hiccreboot.backend.common.consts.HttpConstants.NOT_FOUND;

@AllArgsConstructor
public enum GlobalErrorCode implements BaseErrorCode {
    MEMBER_NOT_FOUND(NOT_FOUND, "USER_NOT_FOUND_404_1", "회원을 찾을 수 없습니다."),
    ARTICLE_NOT_FOUND(NOT_FOUND, "ARTICLE_NOT_FOUND_404_1", "글을 찾을 수 없습니다."),
    SCHEDULE_NOT_FOUND(NOT_FOUND, "SCHEDULE_FOUND_404_1", "스케쥴을 찾을 수 없습니다.");
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
