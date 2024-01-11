package hiccreboot.backend.common.exception;

import hiccreboot.backend.common.exception.dto.ErrorReason;

public interface BaseErrorCode {
	ErrorReason getErrorReason();

	String getExplainError() throws NoSuchFieldException;
}
