package hiccreboot.backend.domains.comment.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hiccreboot.backend.common.auth.jwt.TokenProvider;
import hiccreboot.backend.common.dto.BaseResponse;
import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.domains.comment.dto.request.PostCommentRequest;
import hiccreboot.backend.domains.comment.dto.response.ChildCommentResponse;
import hiccreboot.backend.domains.comment.dto.response.ParentCommentResponse;
import hiccreboot.backend.domains.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;
	private final TokenProvider tokenProvider;

	@GetMapping("/parent/{article-id}")
	@Operation(summary = "부모 댓글 조회", description = "부모 댓글을 조회하는 api")
	public DataResponse<List<ParentCommentResponse>> searchParentComment(@PathVariable("article-id") Long id,
		HttpServletRequest httpServletRequest) {
		String studentNumber = tokenProvider.extractStudentNumber(httpServletRequest).orElse(null);

		return commentService.makeParentComments(id, studentNumber);
	}

	@GetMapping("/child/{article-id}")
	@Operation(summary = "자식 댓글 조회", description = "자식 댓글을 조회하는 api")
	public DataResponse<List<ChildCommentResponse>> searchChildComment(@PathVariable("article-id") Long id,
		HttpServletRequest httpServletRequest) {
		String studentNumber = tokenProvider.extractStudentNumber(httpServletRequest).orElse(null);

		return commentService.makeChildComments(id, studentNumber);
	}

	@PostMapping
	@Operation(summary = "댓글 추가", description = "댓글을 추가하는 api")
	public BaseResponse addComment(@Valid @RequestBody PostCommentRequest postCommentRequest,
		HttpServletRequest httpServletRequest) {
		String studentNumber = tokenProvider.extractStudentNumber(httpServletRequest).orElse(null);
		commentService.saveComment(studentNumber, postCommentRequest);

		return DataResponse.noContent();
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "댓글 삭제", description = "댓글을 삭제하는 api")
	public BaseResponse deleteComment(@PathVariable("id") Long id, HttpServletRequest httpServletRequest) {
		String studentNumber = tokenProvider.extractStudentNumber(httpServletRequest).orElse(null);

		commentService.deleteComment(id, studentNumber);

		return DataResponse.noContent();
	}
}
