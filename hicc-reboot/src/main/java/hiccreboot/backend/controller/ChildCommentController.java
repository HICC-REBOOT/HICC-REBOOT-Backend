package hiccreboot.backend.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hiccreboot.backend.common.dto.ParentComment.ParentCommentRequest;
import hiccreboot.backend.common.exception.CommentNotFoundException;
import hiccreboot.backend.domain.Article;
import hiccreboot.backend.domain.ChildComment;
import hiccreboot.backend.service.ArticleService;
import hiccreboot.backend.service.ChildCommentService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/child-comment")
@RequiredArgsConstructor
public class ChildCommentController {

	private final ChildCommentService childCommentService;
	private final ArticleService articleService;

	@GetMapping("/{article-id}")
	public Object searchParentComment(@PathVariable("article-id") Long id) {
		List<ChildComment> childComments = childCommentService.findChildComments(id);

		// 이 부분에 알맞게 리턴하도록 변경
	}

	@PostMapping
	public Object addParentComment(@RequestBody ParentCommentRequest parentCommentRequest) {
		Optional<Article> article = articleService.findArticle(parentCommentRequest.articleId());
		if (article.isPresent()) {
			childCommentService.saveChildComment(article.get(), member, LocalDateTime.now(),parentCommentRequest.articleId(), parentCommentRequest.content());

			// 이 부분에 상태에 맞게 리턴
			return
		}

		return new CommentNotFoundException();
	}

	@DeleteMapping("/{id}")
	public Object deleteParentComment(@PathVariable("id") Long id) {
		childCommentService.deleteChildComment(id);
	}
}
