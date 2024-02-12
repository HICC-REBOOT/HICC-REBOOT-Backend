package hiccreboot.backend.domains.article.dto.request;

import java.util.List;

import hiccreboot.backend.domains.article.domain.BoardType;
import lombok.Getter;

@Getter
public class ArticleRequest {
	private List<ArticleImageRequest> images;
	private BoardType board;
	private String subject;
	private String content;

}
