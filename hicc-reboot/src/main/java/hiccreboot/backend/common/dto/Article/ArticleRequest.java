package hiccreboot.backend.common.dto.Article;

import java.util.List;

import hiccreboot.backend.common.dto.S3.ImageRequest;
import hiccreboot.backend.domain.BoardType;
import lombok.Getter;

@Getter
public class ArticleRequest {
	private List<ImageRequest> images;
	private BoardType board;
	private String subject;
	private String content;

}
