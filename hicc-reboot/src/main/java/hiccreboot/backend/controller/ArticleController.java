package hiccreboot.backend.controller;

import java.util.HashMap;
import java.util.List;
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

import hiccreboot.backend.common.dto.Article.ArticleListResponse;
import hiccreboot.backend.common.dto.Article.ArticleRequest;
import hiccreboot.backend.common.dto.Article.ArticleResponse;
import hiccreboot.backend.common.exception.ArticleNotFoundException;
import hiccreboot.backend.domain.Article;
import hiccreboot.backend.domain.BoardType;
import hiccreboot.backend.domain.Grade;
import hiccreboot.backend.domain.Member;
import hiccreboot.backend.service.ArticleService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor
public class ArticleController {

	private final ArticleService articleService;
	private final EntityManager em;

	@GetMapping
	public Object searchArticleList(
		@RequestParam(value = "page") int pageNumber,
		@RequestParam(value = "size") int pageSize,
		@RequestParam(value = "board") BoardType boardType,
		@RequestParam(value = "sort", required = false, defaultValue = "null") String sort,
		@RequestParam(value = "search", required = false, defaultValue = "null") String search) {

		//유저 이름,등급 jwt를 통해 가져온다

		List<Article> articles = articleService.findArticleBySort(pageNumber, pageSize, sort, search);

		if (!articles.isEmpty()) {
			ArticleListResponse articleListResponse = new ArticleListResponse();
			articles.stream()
				.forEach(article -> articleListResponse.addResult(
					ArticleResponse.createArticleResponse(article.getId(), userGrade, userName, article.getDate(),
						article.getBoardType(), article.getSubject())));

			// 이 부분 형식에 맞게 리턴
			return articleListResponse;
		}

		return new ArticleNotFoundException();
	}

	@GetMapping("/{article-id}")
	public Object searchArticle(@PathVariable("article-id") Long id) {
		Optional<Article> article = articleService.findArticle(id);

		// 이 부분 jwt로 유저이름이랑, 유저 등급 가져온다.
		Grade userGrade = Grade.APPLICANT;
		String userName = "나야나";

		if (article.isPresent()) {

			// 이 부분 형태에 맞게 return
			return articleService.makeArticleDTO(article.get(), userGrade, userName);
		}
		throw new ArticleNotFoundException();
	}

	@PostMapping("/article")
	public Object addArticle(Member member, @RequestBody ArticleRequest articleRequest) {
		//여기에 jwt로 Member 가져오는 로직 작성, 매개변수의 member 삭제!

		articleService.saveArticle(member, articleRequest.getSubject(), articleRequest.getContent(),
			articleRequest.getBoard(), articleRequest.getDate(), articleRequest.getAppendices());

		//여기에 204 status에 맞게 return 작성
		HashMap<String, Object> returnValues = new HashMap<>();
		returnValues.put("a", "b");
		return returnValues;
	}

	@PatchMapping("/{article-id}")
	public Object updateArticle(
		Member member,
		@PathVariable("article-id") Long id,
		@RequestBody ArticleRequest articleRequest) {
		// Member 부분은 jwt에서 id 찾아서 가져오는 방식으로 수정

		articleService.deleteArticle(id);
		articleService.saveArticle(member, articleRequest.getSubject(), articleRequest.getContent(),
			articleRequest.getBoard(), articleRequest.getDate(), articleRequest.getAppendices());

		//여기에 204 status에 맞게 return 작성
		HashMap<String, Object> returnValues = new HashMap<>();
		returnValues.put("a", "b");
		return returnValues;
	}

	@DeleteMapping("/{article-id}")
	public Object deleteArticle(@PathVariable("article-id") Long id) {
		articleService.deleteArticle(id);

		//여기에 204 status에 맞게 return 작성
		HashMap<String, Object> returnValues = new HashMap<>();
		returnValues.put("a", "b");
		return returnValues;
	}
}
