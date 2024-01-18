package hiccreboot.backend.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hiccreboot.backend.common.auth.jwt.TokenProvider;
import hiccreboot.backend.common.dto.BaseResponse;
import hiccreboot.backend.common.dto.ChildComment.PostChildCommentRequest;
import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.service.ChildCommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/child-comment")
@RequiredArgsConstructor
public class ChildCommentController {

	private final ChildCommentService childCommentService;
	private final TokenProvider tokenProvider;

	@GetMapping("/{article-id}")
	public BaseResponse searchChildComments(@PathVariable("article-id") Long id) {
		return childCommentService.makeChildComments(id);
	}

	@PostMapping
	public BaseResponse addChildComment(@RequestBody PostChildCommentRequest postChildCommentRequest,
		HttpServletRequest httpServletRequest) {
		String studentNumber = tokenProvider.extractStudentNumber(httpServletRequest).orElse(null);

		childCommentService.saveChildComment(studentNumber, postChildCommentRequest.getArticleId(),
			postChildCommentRequest.getParentCommentId(), postChildCommentRequest.getContent());

		return DataResponse.noContent();
	}

	@DeleteMapping("/{id}")
	public BaseResponse deleteChildComment(@PathVariable("id") Long id) {
		childCommentService.deleteChildComment(id);

		return DataResponse.noContent();
	}
}
