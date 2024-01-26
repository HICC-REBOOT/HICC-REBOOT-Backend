package hiccreboot.backend.dto.response;

import hiccreboot.backend.domain.Article;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class PersonalArticleResponse {
	private String subject;
	private String content;
	private Long articleId;

	public static PersonalArticleResponse create(Article article) {
		return PersonalArticleResponse.builder()
			.subject(article.getSubject())
			.content(article.getContent())
			.articleId(article.getId())
			.build();
	}
}
