package hiccreboot.backend.common.exception;

public class DateTimePreconditionFailed extends CustomException {
	public static final CustomException EXCEPTION = new DateTimePreconditionFailed();

	public DateTimePreconditionFailed() {
		super(GlobalErrorCode.DATETIME_PRECONDITION_FAILED);
	}
}
