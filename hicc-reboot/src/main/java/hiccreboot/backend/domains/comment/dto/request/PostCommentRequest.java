package hiccreboot.backend.domains.comment.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PostCommentRequest {

	@Min(1)
	private Long articleId;
	@NotNull
	private Long parentCommentId;
	@NotBlank
	private String content;
}
