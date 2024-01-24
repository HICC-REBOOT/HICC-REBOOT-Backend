package hiccreboot.backend.common.dto.Comment;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import hiccreboot.backend.domain.Grade;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
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

}
