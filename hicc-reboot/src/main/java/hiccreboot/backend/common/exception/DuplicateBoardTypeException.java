package hiccreboot.backend.common.exception;

public class DuplicateBoardTypeException extends CustomException {
	public static final DuplicateBoardTypeException EXCEPTION = new DuplicateBoardTypeException();

	public DuplicateBoardTypeException() {
		super(GlobalErrorCode.DUPLICATE_BOARD_TYPE);
	}
}
