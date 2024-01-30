package hiccreboot.backend.common.exception;

public class PasswordResetNotFoundException extends CustomException {

	public static final CustomException EXCEPTION = new PasswordResetNotFoundException();

	private PasswordResetNotFoundException() {
		super(GlobalErrorCode.PASSWORD_RESET_NOT_FOUND);
	}
}
