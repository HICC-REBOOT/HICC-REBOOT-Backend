package hiccreboot.backend.common.dto.Article;

import java.util.List;

import hiccreboot.backend.domain.BoardType;

public record ArticleRequest(List<String> appendices, BoardType board, String subject,
							 String content) {
}
