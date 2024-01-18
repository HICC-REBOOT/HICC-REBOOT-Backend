package hiccreboot.backend.common.dto.ParentComment;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

@Getter
public class ParentCommentResponse {

	private final Long articleId;
	private final Long commentId;
	private final String name;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	private final LocalDateTime date;
	private final String content;

	public ParentCommentResponse(Long articleId, Long commentId, String name, LocalDateTime date, String content) {
		this.articleId = articleId;
		this.commentId = commentId;
		this.name = name;
		this.date = date;
		this.content = content;
	}
}