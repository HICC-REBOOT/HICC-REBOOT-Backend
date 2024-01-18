package hiccreboot.backend.common.exception;

public class EmailDuplicateException extends CustomException {
	private static final EmailDuplicateException EXCEPTION = new EmailDuplicateException();

	public EmailDuplicateException() {
		super(GlobalErrorCode.EMAIL_DUPLICATE);
	}
}
