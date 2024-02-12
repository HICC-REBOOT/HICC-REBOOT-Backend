package hiccreboot.backend.domains.article.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonFormat;

import hiccreboot.backend.domains.article.domain.Article;
import hiccreboot.backend.domains.article.domain.ArticleGrade;
import hiccreboot.backend.domains.article.domain.BoardType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class
ArticleResponse {
	private final Long articleId;
	private final ArticleGrade grade;
	private final String name;
	private final Boolean isMine;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private final LocalDateTime date;
	private final List<ArticleImageResponse> images;
	private final BoardType board;
	private final String subject;
	private final String content;

	@Builder(access = AccessLevel.PRIVATE)
	private ArticleResponse(
		Long articleId,
		ArticleGrade grade,
		String name,
		Boolean isMine,
		LocalDateTime date,
		List<ArticleImageResponse> images,
		BoardType board,
		String subject,
		String content) {
		this.articleId = articleId;
		this.grade = grade;
		this.name = name;
		this.isMine = isMine;
		this.date = date;
		this.images = images;
		this.board = board;
		this.subject = subject;
		this.content = content;
	}

	public static ArticleResponse create(Article article, Boolean isMine) {
		return ArticleResponse.builder()
			.articleId(article.getId())
			.grade(article.getArticleGrade())
			.name(article.getMemberName())
			.isMine(isMine)
			.date(article.getDate())
			.images(
				article.getImages()
					.stream()
					.map(ArticleImageResponse::create)
					.collect(Collectors.toList()))
			.board(article.getBoardType())
			.subject(article.getSubject())
			.content(article.getContent())
			.build();
	}
}
