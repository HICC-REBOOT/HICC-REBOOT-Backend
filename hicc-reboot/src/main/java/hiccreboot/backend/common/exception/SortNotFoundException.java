package hiccreboot.backend.common.exception;

public class SortNotFoundException extends CustomException {

	public static final CustomException EXCEPTION = new SortNotFoundException();

	public SortNotFoundException() {
		super(GlobalErrorCode.SORT_NOT_FOUND);
	}
}
