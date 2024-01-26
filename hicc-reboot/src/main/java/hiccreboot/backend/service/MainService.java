package hiccreboot.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.common.dto.Main.LatestNewsResponse;
import hiccreboot.backend.domain.Article;
import hiccreboot.backend.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MainService {

	private final MemberRepository memberRepository;
	private final ArticleService articleService;

	public Long findMemberCount() {
		return memberRepository.count();
	}

	public DataResponse<List<LatestNewsResponse>> makeLatestNews(int pageNumber, int pageSize) {
		List<Article> articles = articleService.findArticles(pageNumber, pageSize).getContent();

		List<LatestNewsResponse> latestNewsResponses = new ArrayList<>();
		articles.stream()
			.forEach(article -> latestNewsResponses.add(LatestNewsResponse.create(article)));

		return DataResponse.ok(latestNewsResponses);
	}
}
