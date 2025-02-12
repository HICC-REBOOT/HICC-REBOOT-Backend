package hiccreboot.backend.domains.article.controller;

import java.util.List;

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
import hiccreboot.backend.common.dto.BaseResponse;
import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.common.exception.MemberNotFoundException;
import hiccreboot.backend.domains.article.domain.ArticleGrade;
import hiccreboot.backend.domains.article.domain.BoardType;
import hiccreboot.backend.domains.article.dto.request.ArticleRequest;
import hiccreboot.backend.domains.article.dto.request.CreateBoardTypeRequest;
import hiccreboot.backend.domains.article.dto.response.ArticleResponse;
import hiccreboot.backend.domains.article.dto.response.FindBoardTypesResponse;
import hiccreboot.backend.domains.article.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor
public class ArticleController {

	private final ArticleService articleService;
	private final TokenProvider tokenProvider;

	@GetMapping
	@Operation(summary = "게시글 목록 조회")
	public BaseResponse searchArticleList(
		@RequestParam(value = "page") int pageNumber,
		@RequestParam(value = "size") int pageSize,
		@RequestParam(value = "board") BoardType boardType,
		@RequestParam(value = "articleGrade", required = false, defaultValue = "NORMAL") ArticleGrade articleGrade,
		@RequestParam(value = "findBy", required = false, defaultValue = "ARTICLE") String findBy,
		@RequestParam(value = "search", required = false, defaultValue = "") String search) {

		return articleService.makeArticles(pageNumber, pageSize, boardType, articleGrade,
			findBy, search);
	}

	@GetMapping("/{article-id}")
	@Operation(summary = "게시글 상세 조회")
	public DataResponse<ArticleResponse> searchArticle(@PathVariable("article-id") Long id,
		HttpServletRequest httpServletRequest) {
		String studentNumber = tokenProvider.extractStudentNumber(httpServletRequest)
			.orElseThrow(() -> MemberNotFoundException.EXCEPTION);
		return articleService.makeArticle(id, studentNumber);
	}

	@PostMapping
	@Operation(summary = "게시글 작성")
	public BaseResponse addArticle(@Valid @RequestBody ArticleRequest articleRequest,
		HttpServletRequest httpServletRequest) {
		String studentNumber = tokenProvider.extractStudentNumber(httpServletRequest).orElse(null);

		articleService.saveArticle(studentNumber, articleRequest);

		return DataResponse.noContent();
	}

	@PatchMapping("/{article-id}")
	@Operation(summary = "게시글 수정")
	public BaseResponse updateArticle(
		@PathVariable("article-id") Long id,
		@Valid @RequestBody ArticleRequest articleRequest, HttpServletRequest httpServletRequest) {
		String studentNumber = tokenProvider.extractStudentNumber(httpServletRequest).orElse(null);
		articleService.updateArticle(id, articleRequest, studentNumber);

		return DataResponse.noContent();
	}

	@DeleteMapping("/{article-id}")
	@Operation(summary = "게시글 삭제")
	public BaseResponse deleteArticle(@PathVariable("article-id") Long id, HttpServletRequest httpServletRequest) {
		String studentNumber = tokenProvider.extractStudentNumber(httpServletRequest).orElse(null);

		articleService.deleteArticle(id, studentNumber);

		return DataResponse.noContent();
	}

	@GetMapping("/board-types")
	@Operation(summary = "게시판 목록 조회")
	public DataResponse<List<FindBoardTypesResponse>> findBoardTypes() {
		List<FindBoardTypesResponse> responses = articleService.findBoardTypes();

		return DataResponse.ok(responses);
	}

	@PostMapping("/board-types")
	@Operation(summary = "게시판 목록 추가")
	public BaseResponse createBoardType(@RequestBody CreateBoardTypeRequest request) {
		articleService.createBoardType(request.name());

		return DataResponse.noContent();
	}

	@DeleteMapping("/board-types/{board-type-id}")
	@Operation(summary = "게시판 목록 삭제")
	public BaseResponse deleteBoardType(@PathVariable("board-type-id") Long boardTypeId) {
		articleService.deleteBoardType(boardTypeId);

		return DataResponse.noContent();
	}

	@PatchMapping("/board-types/{board-type-id}")
	@Operation(summary = "게시판 목록 수정")
	public BaseResponse updateBoardType(
		@PathVariable("board-type-id") Long boardTypeId,
		@RequestBody CreateBoardTypeRequest request
	) {
		articleService.updateBoardType(boardTypeId, request.name());

		return DataResponse.noContent();
	}
}
