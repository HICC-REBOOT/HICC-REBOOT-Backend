package hiccreboot.backend.common.dto.Article;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import hiccreboot.backend.common.dto.S3.ImageRequest;
import hiccreboot.backend.domain.BoardType;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ArticleRequest {
	private List<ImageRequest> images;
	private BoardType board;
	private String subject;
	private String content;

}
