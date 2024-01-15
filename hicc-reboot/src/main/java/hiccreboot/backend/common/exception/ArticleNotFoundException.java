package hiccreboot.backend.common.exception;

public class ArticleNotFoundException extends CustomException {
    public static final CustomException EXCEPTION = new ArticleNotFoundException();

    public ArticleNotFoundException() {
        super(GlobalErrorCode.ARTICLE_NOT_FOUND);
    }
}
