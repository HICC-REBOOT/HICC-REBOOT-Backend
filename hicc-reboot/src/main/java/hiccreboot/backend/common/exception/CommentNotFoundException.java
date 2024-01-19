package hiccreboot.backend.common.exception;

public class CommentNotFoundException extends CustomException {

	public static final CustomException EXCEPTION = new CommentNotFoundException();

	public CommentNotFoundException() {
		super(GlobalErrorCode.COMMENT_NOT_FOUND);
	}
}
