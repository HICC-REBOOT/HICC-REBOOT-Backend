package hiccreboot.backend.common.exception;

public class FileNameExtenstionNotFoundException extends CustomException {

	public static final CustomException EXCEPTION = new FileNameExtenstionNotFoundException();

	public FileNameExtenstionNotFoundException() {
		super(GlobalErrorCode.FILE_NAME_EXTENSION_NOT_FOUND);
	}
}
