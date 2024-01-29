package hiccreboot.backend.common.exception;

public class ImageCountTooLarge extends CustomException {

	public static final CustomException EXCEPTION = new ImageCountTooLarge();

	public ImageCountTooLarge() {
		super(GlobalErrorCode.IMAGE_COUNT_TOO_LARGE);
	}
}
