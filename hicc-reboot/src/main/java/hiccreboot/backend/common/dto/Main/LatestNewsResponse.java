package hiccreboot.backend.common.dto.Main;

import java.time.LocalDateTime;

import hiccreboot.backend.domain.Article;
import hiccreboot.backend.domain.Grade;
import lombok.Getter;

@Getter
public class LatestNewsResponse {
	private final Long articleId;
	private final Grade grade;
	private final String name;
	private final LocalDateTime date;
	private final Boolean imageExistence;
	private final String subject;

	private LatestNewsResponse(Long articleId, Grade grade, String name, LocalDateTime date, Boolean imageExistence,
		String subject) {
		this.articleId = articleId;
		this.grade = grade;
		this.name = name;
		this.date = date;
		this.imageExistence = imageExistence;
		this.subject = subject;
	}

	public static LatestNewsResponse create(Article article) {
		return new LatestNewsResponse(
			article.getId(),
			article.getMember().getGrade(),
			article.getMember().getName(),
			article.getDate(),
			!article.getImages().isEmpty(),
			article.getSubject());
	}
}
