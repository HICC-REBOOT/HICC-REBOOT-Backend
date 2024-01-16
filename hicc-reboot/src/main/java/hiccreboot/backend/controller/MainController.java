package hiccreboot.backend.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hiccreboot.backend.common.dto.Main.LatestNewsResponse;
import hiccreboot.backend.domain.Article;
import hiccreboot.backend.service.ArticleService;
import hiccreboot.backend.service.MainService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/main")
@RequiredArgsConstructor
public class MainController {

	private final MainService mainService;
	private final ArticleService articleService;

	@GetMapping("/member-count")
	public Object searchMemberCount() {
		Long memberCount = mainService.findMemberCount();

		// 이 부분에 리턴 형식에 맞게 리턴
	}

	@GetMapping("/latest-news")
	public Object searchLatestNews(
		@RequestParam(value = "page") int pageNumber,
		@RequestParam(value = "size") int pageSize) {
		List<Article> articles = articleService.findArticles(pageNumber, pageSize).getContent();

		// jwt로 grade 가져온다.

		List<LatestNewsResponse> latestNewsResponses = new ArrayList<>();
		articles.stream().forEach(article -> latestNewsResponses.add(new LatestNewsResponse(
			article.getId(),
			grade,
			name,
			article.getDate(),
			!article.getAppendices().isEmpty(),
			article.getSubject())));

		// 이 부분에 형식에 맞게 리턴
	}
}


