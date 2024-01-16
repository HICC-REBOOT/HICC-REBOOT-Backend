package hiccreboot.backend.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hiccreboot.backend.common.dto.BaseResponse;
import hiccreboot.backend.common.dto.ParentComment.ParentCommentRequest;
import hiccreboot.backend.common.exception.CommentNotFoundException;
import hiccreboot.backend.domain.Article;
import hiccreboot.backend.service.ArticleService;
import hiccreboot.backend.service.ParentCommentService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/parent-comment")
@RequiredArgsConstructor
public class ParentCommentController {

	private final ParentCommentService parentCommentService;
	private final ArticleService articleService;

	@GetMapping("/{article-id}")
	public Object searchParentComment(@PathVariable("article-id") Long id) {

		// 이부분 형식에 맞게 리턴하도록 변경
		return parentCommentService.findParentComments(id);
	}

	@PostMapping
	public Object addParentComment(@RequestBody ParentCommentRequest parentCommentRequest) {
		Optional<Article> article = articleService.findArticle(parentCommentRequest.articleId());

		// 이 부분에 jwt로 멤버가져오기

		if (article.isPresent()) {
			parentCommentService.saveParentComment(article.get(), member, LocalDateTime.now(),
				parentCommentRequest.content());

			// 이 부분에 성공에 맞는 리턴 값 주기
			return new BaseResponse();
		}
		return new CommentNotFoundException();
	}

	@DeleteMapping("/{id}")
	public Object deleteParentComment(@PathVariable("id") Long id) {
		parentCommentService.deleteParentComment(id);

		// 이 부분 status에 맞게 변경

	}
}
