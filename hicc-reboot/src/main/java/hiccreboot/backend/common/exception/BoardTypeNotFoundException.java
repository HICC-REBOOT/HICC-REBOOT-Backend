package hiccreboot.backend.common.exception;

public class BoardTypeNotFoundException extends CustomException {
	public static final CustomException EXCEPTION = new BoardTypeNotFoundException();

	public BoardTypeNotFoundException() {
		super(GlobalErrorCode.BOARD_TYPE_NOT_FOUND);
	}
}
