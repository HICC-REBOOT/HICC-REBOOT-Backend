package hiccreboot.backend.common.exception;

public class AccessForbiddenException extends CustomException {
	public static final CustomException EXCEPTION = new AccessForbiddenException();

	public AccessForbiddenException() {
		super(GlobalErrorCode.ACCESS_FORBIDDEN);
	}
}
