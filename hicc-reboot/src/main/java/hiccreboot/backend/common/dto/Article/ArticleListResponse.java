package hiccreboot.backend.common.dto.Article;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import hiccreboot.backend.domain.Article;
import hiccreboot.backend.domain.BoardType;
import hiccreboot.backend.domain.Grade;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ArticleListResponse {
	private final Long articleId;
	private final Grade grade;
	private final String name;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private final LocalDateTime date;
	private final BoardType board;
	private final Boolean imageExistence;
	private final String subject;

	@Builder(access = AccessLevel.PRIVATE)
	private ArticleListResponse(
		Long articleId,
		Grade grade,
		String name,
		LocalDateTime date,
		BoardType board,
		Boolean imageExistence,
		String subject) {
		this.articleId = articleId;
		this.grade = grade;
		this.name = name;
		this.date = date;
		this.board = board;
		this.imageExistence = imageExistence;
		this.subject = subject;
	}

	public static ArticleListResponse create(Article article) {
		return ArticleListResponse.builder()
			.articleId(article.getId())
			.grade(article.getMember().getGrade())
			.name(article.getMember().getName())
			.date(article.getDate())
			.board(article.getBoardType())
			.imageExistence(!article.getImages().isEmpty())
			.subject(article.getSubject())
			.build();
	}
}
