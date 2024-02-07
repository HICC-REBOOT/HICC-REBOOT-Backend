package hiccreboot.backend.controller;

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
import hiccreboot.backend.common.dto.Comment.ChildCommentResponse;
import hiccreboot.backend.common.dto.Comment.ParentCommentResponse;
import hiccreboot.backend.common.dto.Comment.PostCommentRequest;
import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;
	private final TokenProvider tokenProvider;

	@GetMapping("/parent/{article-id}")
	public DataResponse<List<ParentCommentResponse>> searchParentComment(@PathVariable("article-id") Long id,
		HttpServletRequest httpServletRequest) {
		String studentNumber = tokenProvider.extractStudentNumber(httpServletRequest).orElse(null);

		return commentService.makeParentComments(id, studentNumber);
	}

	@GetMapping("/child/{article-id}")
	public DataResponse<List<ChildCommentResponse>> searchChildComment(@PathVariable("article-id") Long id,
		HttpServletRequest httpServletRequest) {
		String studentNumber = tokenProvider.extractStudentNumber(httpServletRequest).orElse(null);

		return commentService.makeChildComments(id, studentNumber);
	}

	@PostMapping
	public BaseResponse addComment(@RequestBody PostCommentRequest postCommentRequest,
		HttpServletRequest httpServletRequest) {
		String studentNumber = tokenProvider.extractStudentNumber(httpServletRequest).orElse(null);
		commentService.saveComment(studentNumber, postCommentRequest);

		return DataResponse.noContent();
	}

	@DeleteMapping("/{id}")
	public BaseResponse deleteComment(@PathVariable("id") Long id, HttpServletRequest httpServletRequest) {
		String studentNumber = tokenProvider.extractStudentNumber(httpServletRequest).orElse(null);

		commentService.deleteComment(id, studentNumber);

		return DataResponse.noContent();
	}
}
