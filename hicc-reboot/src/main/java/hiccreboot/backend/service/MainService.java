package hiccreboot.backend.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.common.dto.Main.LatestNewsResponse;
import hiccreboot.backend.common.exception.MemberNotFoundException;
import hiccreboot.backend.domain.Article;
import hiccreboot.backend.domain.Grade;
import hiccreboot.backend.domain.Member;
import hiccreboot.backend.dto.response.FooterResponse;
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

	public DataResponse<FooterResponse> footerResponse() {
		FooterResponse result = memberRepository.findAllByGrade(Grade.PRESIDENT)
			.stream()
			.max(Comparator.comparing(Member::getId))
			.map(FooterResponse::create)
			.orElseThrow(() -> MemberNotFoundException.EXCEPTION);

		return DataResponse.ok(result);
	}
}
