package hiccreboot.backend.controller;

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
import hiccreboot.backend.common.dto.BaseResponse;
import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.common.exception.ArticleNotFoundException;
import hiccreboot.backend.common.exception.MemberNotFoundException;
import hiccreboot.backend.domain.Article;
import hiccreboot.backend.domain.BoardType;
import hiccreboot.backend.service.ArticleService;
import hiccreboot.backend.service.S3Service;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor
public class ArticleController {

	private final ArticleService articleService;
	private final S3Service s3Service;
	private final TokenProvider tokenProvider;

	@GetMapping
	public BaseResponse searchArticleList(
		@RequestParam(value = "page") int pageNumber,
		@RequestParam(value = "size") int pageSize,
		@RequestParam(value = "board") BoardType boardType,
		@RequestParam(value = "sort", required = false, defaultValue = "article") String sort,
		@RequestParam(value = "search", required = false, defaultValue = "null") String search) {

		return articleService.makeArticles(pageNumber, pageSize, boardType,
			sort, search);
	}

	@GetMapping("/{article-id}")
	public BaseResponse searchArticle(@PathVariable("article-id") Long id, HttpServletRequest httpServletRequest) {
		String studentNumber = tokenProvider.extractStudentNumber(httpServletRequest)
			.orElseThrow(() -> MemberNotFoundException.EXCEPTION);
		return articleService.makeArticle(id, studentNumber);
	}

	@PostMapping
	public BaseResponse addArticle(@RequestBody ArticleRequest articleRequest, HttpServletRequest httpServletRequest) {
		String studentNumber = tokenProvider.extractStudentNumber(httpServletRequest).orElse(null);

		articleService.saveArticle(studentNumber, articleRequest);

		return DataResponse.noContent();
	}

	@PatchMapping("/{article-id}")
	public BaseResponse updateArticle(
		@PathVariable("article-id") Long id,
		@RequestBody ArticleRequest articleRequest) {

		articleService.updateArticle(id, articleRequest);

		return DataResponse.noContent();
	}

	@DeleteMapping("/{article-id}")
	public BaseResponse deleteArticle(@PathVariable("article-id") Long id) {
		Article article = articleService.findArticle(id).orElseThrow(() -> ArticleNotFoundException.EXCEPTION);

		article.getImages()
			.forEach(image -> s3Service.deleteImage(image.getFileName()));

		articleService.deleteArticle(id);

		return DataResponse.noContent();
	}
}
