package hiccreboot.backend.common.exception;

public class StudentNumberDuplicationException extends CustomException {

	public static final CustomException EXCEPTION = new StudentNumberDuplicationException();

	public StudentNumberDuplicationException() {
		super(GlobalErrorCode.STUDENT_NUMBER_DUPLICATION);
	}
}
