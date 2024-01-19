package hiccreboot.backend.common.dto.Article;

import java.util.List;

import hiccreboot.backend.common.dto.S3.ImageDTO;
import hiccreboot.backend.domain.BoardType;

public record ArticleRequest(List<ImageDTO> images, BoardType board, String subject,
							 String content) {
}
