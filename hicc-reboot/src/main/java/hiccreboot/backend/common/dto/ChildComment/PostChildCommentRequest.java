package hiccreboot.backend.common.dto.ChildComment;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class PostChildCommentRequest {
	private Long articleId;
	private Long parentCommentId;
	private String content;

}
