package hiccreboot.backend.common.dto.Article;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import hiccreboot.backend.domain.Appendix;
import hiccreboot.backend.domain.BoardType;
import hiccreboot.backend.domain.Grade;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ArticleResponse {
	private final Long articleId;
	private final Grade grade;
	private final String name;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
	private final LocalDateTime date;
	private final Boolean appendixExistence;
	private final List<Appendix> appendices;
	private final BoardType board;
	private final String subject;
	private final String content;

	@Builder
	private ArticleResponse(
		Long articleId,
		Grade grade,
		String name,
		LocalDateTime date,
		Boolean appendixExistence,
		List<Appendix> appendices,
		BoardType board,
		String subject,
		String content) {
		this.articleId = articleId;
		this.grade = grade;
		this.name = name;
		this.date = date;
		this.appendixExistence = appendixExistence;
		this.appendices = appendices;
		this.board = board;
		this.subject = subject;
		this.content = content;
	}

	public static ArticleResponse createArticleResponse(
		Long articleId,
		Grade grade,
		String name,
		LocalDateTime date,
		Boolean appendixExistence,
		List<Appendix> appendices,
		BoardType board,
		String subject,
		String content
	) {
		return ArticleResponse.builder()
			.articleId(articleId)
			.grade(grade)
			.name(name)
			.date(date)
			.appendixExistence(appendixExistence)
			.appendices(appendices)
			.board(board)
			.subject(subject)
			.content(content)
			.build();
	}

	public static ArticleResponse createArticleResponse(
		Long articleId,
		Grade grade,
		String name,
		LocalDateTime date,
		BoardType board,
		String subject
	) {
		return ArticleResponse.builder()
			.articleId(articleId)
			.grade(grade)
			.name(name)
			.date(date)
			.board(board)
			.subject(subject)
			.build();
	}
}
