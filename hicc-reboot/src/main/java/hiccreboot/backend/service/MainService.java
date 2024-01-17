package hiccreboot.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import hiccreboot.backend.common.dto.DataListResponse;
import hiccreboot.backend.common.dto.Main.LatestNewsResponse;
import hiccreboot.backend.common.exception.ArticleNotFoundException;
import hiccreboot.backend.domain.Article;
import hiccreboot.backend.domain.Grade;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MainService {

	private final MemberRepository memberRepository;
	private final ArticleService articleService;

	public Long findMemberCount() {
		return MemberRepository.countBy();
	}

	public DataListResponse<LatestNewsResponse> makeLatestNews(int pageNumber, int pageSize, Grade memberGrade,
		String memberName) {
		List<Article> articles = articleService.findArticles(pageNumber, pageSize).getContent();

		if (articles.isEmpty()) {
			throw ArticleNotFoundException.EXCEPTION;
		}

		List<LatestNewsResponse> latestNewsResponses = new ArrayList<>();
		articles.stream()
			.forEach(article -> latestNewsResponses.add(new LatestNewsResponse(
				article.getId(),
				memberGrade,
				memberName,
				article.getDate(),
				!article.getAppendices().isEmpty(),
				article.getSubject())));
		return DataListResponse.create(latestNewsResponses);
	}
}
