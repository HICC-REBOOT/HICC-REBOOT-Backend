package hiccreboot.backend.common.dto.ChildComment;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

@Getter
public class ChildCommentResponse {

	private final Long articleId;
	private final Long parentCommentId;
	private final Long commentId;
	private final String name;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	private final LocalDateTime date;
	private final String content;

	public ChildCommentResponse(Long articleId, Long parentCommentId, Long commentId, String name, LocalDateTime date,
		String content) {
		this.articleId = articleId;
		this.parentCommentId = parentCommentId;
		this.commentId = commentId;
		this.name = name;
		this.date = date;
		this.content = content;
	}
}
