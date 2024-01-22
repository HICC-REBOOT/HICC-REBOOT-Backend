package hiccreboot.backend.common.dto.Article;

import java.util.List;

import hiccreboot.backend.common.dto.S3.ImageRequest;
import hiccreboot.backend.domain.BoardType;

public record ArticleRequest(List<ImageRequest> images, BoardType board, String subject,
							 String content) {
}
