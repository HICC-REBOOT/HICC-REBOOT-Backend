package hiccreboot.backend.common.dto.Article;

import java.util.List;

import hiccreboot.backend.common.dto.S3.ImageRequest;
import hiccreboot.backend.domain.BoardType;
import lombok.Getter;

@Getter
public class ArticleRequest {
	private final List<ImageRequest> images;
	private final BoardType board;
	private final String subject;
	private final String content;

	private ArticleRequest(List<ImageRequest> images, BoardType board, String subject, String content) {
		this.images = images;
		this.board = board;
		this.subject = subject;
		this.content = content;
	}

	public ArticleRequest create(List<ImageRequest> images, BoardType board, String subject, String content) {
		return new ArticleRequest(images, board, subject, content);
	}
}
