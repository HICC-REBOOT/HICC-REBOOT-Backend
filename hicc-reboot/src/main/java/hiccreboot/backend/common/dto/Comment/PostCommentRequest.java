package hiccreboot.backend.common.dto.Comment;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PostCommentRequest {

	private Long articleId;
	private Long parentCommentId;
	private String content;
}
