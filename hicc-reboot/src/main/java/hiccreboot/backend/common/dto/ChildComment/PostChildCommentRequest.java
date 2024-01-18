package hiccreboot.backend.common.dto.ChildComment;

import lombok.Getter;

@Getter
public class PostChildCommentRequest {
	private final Long articleId;
	private final Long parentCommentId;
	private final String content;

	public PostChildCommentRequest(Long articleId, Long parentCommentId, String content) {
		this.articleId = articleId;
		this.parentCommentId = parentCommentId;
		this.content = content;
	}
}
