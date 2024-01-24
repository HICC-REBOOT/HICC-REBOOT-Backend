package hiccreboot.backend.common.dto.ParentComment;

import lombok.Getter;

@Getter
public class PostParentCommentRequest {

	private Long articleId;
	private String content;

}
