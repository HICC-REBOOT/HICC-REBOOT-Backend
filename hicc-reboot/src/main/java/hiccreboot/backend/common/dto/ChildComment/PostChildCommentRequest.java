package hiccreboot.backend.common.dto.ChildComment;

import lombok.Getter;

@Getter
public class PostChildCommentRequest {
	private Long articleId;
	private Long parentCommentId;
	private String content;

}
