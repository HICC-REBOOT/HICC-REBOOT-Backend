package hiccreboot.backend.controller;

import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hiccreboot.backend.common.auth.jwt.TokenProvider;
import hiccreboot.backend.common.dto.Article.ArticleRequest;
import hiccreboot.backend.common.dto.Article.ArticleResponse;
import hiccreboot.backend.common.dto.Article.ArticlesResponse;
import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.domain.BoardType;
import hiccreboot.backend.domain.Member;
import hiccreboot.backend.service.ArticleService;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor
public class ArticleController {

	private final ArticleService articleService;
	private final EntityManager em;
	private final TokenProvider tokenProvider;
	private final MemberService memberService;

	@GetMapping
	public DataResponse<ArticlesResponse> searchArticleList(
		@RequestParam(value = "page") int pageNumber,
		@RequestParam(value = "size") int pageSize,
		@RequestParam(value = "board") BoardType boardType,
		@RequestParam(value = "sort", required = false, defaultValue = "article") String sort,
		@RequestParam(value = "search", required = false, defaultValue = "null") String search) {

		return articleService.makeArticles(pageNumber, pageSize, boardType,
			sort, search);
	}

	@GetMapping("/{article-id}")
	public DataResponse<ArticleResponse> searchArticle(@PathVariable("article-id") Long id) {
		return articleService.makeArticle(id);
	}

	@PostMapping("/article")
	public DataResponse addArticle(@RequestBody ArticleRequest articleRequest, HttpServletRequest httpServletRequest) {
		Optional<String> studentNumber = tokenProvider.extractStudentNumber(httpServletRequest);
		Member member = memberService.findMemberByStudentNumber(studentNumber.get());

		articleService.saveArticle(member, articleRequest.getSubject(), articleRequest.getContent(),
			articleRequest.getBoard(), articleRequest.getAppendices());

		return DataResponse.noContent();
	}

	@PatchMapping("/{article-id}")
	public Object updateArticle(
		@PathVariable("article-id") Long id,
		@RequestBody ArticleRequest articleRequest) {

		//update 구현
		articleService.updateArticle();

		return DataResponse.noContent();
	}

	@DeleteMapping("/{article-id}")
	public DataResponse deleteArticle(@PathVariable("article-id") Long id) {
		articleService.deleteArticle(id);

		return DataResponse.noContent();
	}
}
