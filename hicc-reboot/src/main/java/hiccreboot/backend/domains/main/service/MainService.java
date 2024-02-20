package hiccreboot.backend.domains.main.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.common.exception.MemberNotFoundException;
import hiccreboot.backend.domains.article.domain.Article;
import hiccreboot.backend.domains.article.service.ArticleService;
import hiccreboot.backend.domains.main.dto.response.FooterResponse;
import hiccreboot.backend.domains.main.dto.response.LatestNewsResponse;
import hiccreboot.backend.domains.member.domain.Grade;
import hiccreboot.backend.domains.member.domain.Member;
import hiccreboot.backend.domains.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MainService {

	private final MemberRepository memberRepository;
	private final ArticleService articleService;

	public Long findMemberCount() {
		return memberRepository.countByGradeNot(Grade.APPLICANT);
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
