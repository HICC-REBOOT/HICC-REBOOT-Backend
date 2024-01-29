package hiccreboot.backend.common.exception.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import hiccreboot.backend.common.dto.BaseResponse;
import hiccreboot.backend.common.exception.BaseErrorCode;
import hiccreboot.backend.common.exception.CustomException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorResponse extends BaseResponse {

	private final int status;
	private final String code;
	private final String reason;
	private final String path;

	private ErrorResponse(HttpStatusCode status, String reason, String path) {
		super(false, status);
		this.status = status.value();
		this.code = HttpStatus.valueOf(status.value()).getReasonPhrase();
		this.reason = reason;
		this.path = path;
	}

	@Builder(access = AccessLevel.PRIVATE)
	private ErrorResponse(ErrorReason errorReason, String path) {
		super(false, HttpStatusCode.valueOf(errorReason.getStatus()));
		this.status = errorReason.getStatus();
		this.code = errorReason.getCode();
		this.reason = errorReason.getReason();
		this.path = path;
	}

	public static ErrorResponse fromCustomException(CustomException e, String path) {
		return ErrorResponse.builder()
			.errorReason(e.getErrorReason())
			.path(path)
			.build();
	}

	public static ErrorResponse fromErrorCode(BaseErrorCode errorCode, String path) {
		return ErrorResponse.builder()
			.errorReason(errorCode.getErrorReason())
			.path(path)
			.build();
	}
}
