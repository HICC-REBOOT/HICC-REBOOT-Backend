package hiccreboot.backend.domains.member.dto.response;

import hiccreboot.backend.domains.article.domain.Article;
import hiccreboot.backend.domains.comment.domain.Comment;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class PersonalCommentResponse {
	private String subject;
	private String content;
	private Long articleId;
	private Long commentId;

	public static PersonalCommentResponse create(Comment comment) {
		Article article = comment.getArticle();

		return PersonalCommentResponse.builder()
			.subject(article.getSubject())
			.content(comment.getContent())
			.articleId(article.getId())
			.commentId(comment.getId())
			.build();
	}
}
