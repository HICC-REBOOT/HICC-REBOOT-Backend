package hiccreboot.backend.common.dto.Article;

import java.util.List;

import hiccreboot.backend.domain.BoardType;
import lombok.Getter;

@Getter
public class ArticleRequest {
	private List<ArticleImageRequest> images;
	private BoardType board;
	private String subject;
	private String content;

}
