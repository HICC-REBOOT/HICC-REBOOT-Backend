package hiccreboot.backend.common.dto.ParentComment;

import lombok.Getter;

@Getter
public class PostParentCommentRequest {

	private final Long articleId;
	private final String content;

	public PostParentCommentRequest(Long articleId, String content) {
		this.articleId = articleId;
		this.content = content;
	}
}
