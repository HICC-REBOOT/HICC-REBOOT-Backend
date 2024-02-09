package hiccreboot.backend.common.exception;

public class EmailMismatchException extends CustomException {

	public static final CustomException EXCEPTION = new EmailMismatchException();

	private EmailMismatchException() {
		super(GlobalErrorCode.EMAIL_MISMATCH);
	}
}
