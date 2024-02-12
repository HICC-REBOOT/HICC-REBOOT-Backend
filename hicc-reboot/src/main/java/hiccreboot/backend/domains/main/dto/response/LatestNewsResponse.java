package hiccreboot.backend.domains.main.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import hiccreboot.backend.domains.article.domain.Article;
import hiccreboot.backend.domains.article.domain.ArticleGrade;
import lombok.Getter;

@Getter
public class LatestNewsResponse {
	private final Long articleId;
	private final ArticleGrade grade;
	private final String name;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	private final LocalDateTime date;
	private final Boolean imageExistence;
	private final String subject;

	private LatestNewsResponse(Long articleId, ArticleGrade grade, String name, LocalDateTime date,
		Boolean imageExistence,
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
			article.getArticleGrade(),
			article.getMemberName(),
			article.getDate(),
			!article.getImages().isEmpty(),
			article.getSubject());
	}
}
