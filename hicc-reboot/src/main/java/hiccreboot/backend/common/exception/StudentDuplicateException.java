package hiccreboot.backend.common.exception;

public class StudentDuplicateException extends CustomException {

	public static final CustomException EXCEPTION = new StudentDuplicateException();

	private StudentDuplicateException() {
		super(GlobalErrorCode.STUDENT_NUMBER_DUPLICATE);
	}
}
