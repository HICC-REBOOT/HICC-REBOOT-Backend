package hiccreboot.backend.common.dto.Article;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import hiccreboot.backend.domain.Appendix;
import hiccreboot.backend.domain.BoardType;
import hiccreboot.backend.domain.Grade;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Getter
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ArticleRequest {
	private final Long articleId;
	private final Grade grade;
	private final String name;
	private final LocalDateTime date;
	private final Boolean appendixExistence;
	private final List<Appendix> appendices;
	private final BoardType board;
	private final String subject;
	private final String content;
}


