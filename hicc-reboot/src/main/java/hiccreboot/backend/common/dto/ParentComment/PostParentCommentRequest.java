package hiccreboot.backend.common.dto.ParentComment;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class PostParentCommentRequest {

	private Long articleId;
	private String content;

}
