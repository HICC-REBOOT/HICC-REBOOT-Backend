package hiccreboot.backend.domains.comment.dto.request;

import lombok.Getter;

@Getter
public class PostCommentRequest {

	private Long articleId;
	private Long parentCommentId;
	private String content;
}
