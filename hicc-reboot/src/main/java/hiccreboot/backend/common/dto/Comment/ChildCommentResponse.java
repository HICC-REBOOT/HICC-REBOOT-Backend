package hiccreboot.backend.common.dto.Comment;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import hiccreboot.backend.domain.Comment;
import hiccreboot.backend.domain.Grade;
import lombok.Getter;

@Getter
public class ChildCommentResponse {

	private final Long articleId;
	private final Long parentCommentId;
	private final Long commentId;
	private final String name;
	private final Grade grade;
	private final Boolean isMine;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	private final LocalDateTime date;
	private final String content;

	public ChildCommentResponse(Comment comment, Boolean isMine) {
		this.articleId = comment.getArticle().getId();
		this.parentCommentId = comment.getParentCommentId();
		this.commentId = comment.getId();
		this.name = comment.getMember().getName();
		this.grade = comment.getMember().getGrade();
		this.isMine = isMine;
		this.date = comment.getDate();
		this.content = comment.getContent();
	}
}