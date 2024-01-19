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
import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.common.dto.ParentComment.PostParentCommentRequest;
import hiccreboot.backend.service.ParentCommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/parent-comment")
@RequiredArgsConstructor
public class ParentCommentController {

	private final ParentCommentService parentCommentService;
	private final TokenProvider tokenProvider;

	@GetMapping("/{article-id}")
	public BaseResponse searchParentComment(@PathVariable("article-id") Long id) {
		return parentCommentService.makeParentComments(id);
	}

	@PostMapping
	public BaseResponse addParentComment(@RequestBody PostParentCommentRequest postParentCommentRequest,
		HttpServletRequest httpServletRequest) {
		String studentNumber = tokenProvider.extractStudentNumber(httpServletRequest).orElse(null);
		parentCommentService.saveParentComment(postParentCommentRequest.getArticleId(), studentNumber,
			postParentCommentRequest.getContent());

		return DataResponse.noContent();
	}

	@DeleteMapping("/{id}")
	public BaseResponse deleteParentComment(@PathVariable("id") Long id) {
		parentCommentService.deleteParentComment(id);

		return DataResponse.noContent();
	}
}
