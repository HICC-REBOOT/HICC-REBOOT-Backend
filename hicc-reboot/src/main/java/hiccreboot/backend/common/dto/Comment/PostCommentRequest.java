package hiccreboot.backend.common.dto.Comment;

import lombok.Getter;

@Getter
public class PostCommentRequest {

	private Long articleId;
	private Long parentCommentId;
	private String content;

}
