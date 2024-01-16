package hiccreboot.backend.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import hiccreboot.backend.domain.Article;
import hiccreboot.backend.repository.Article.ArticleRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MainService {

	private final MemberRepository memberRepository;
	private final ArticleRepository articleRepository;

	public Long findMemberCount() {
		return MemberRepository.countBy();
	}

	public Slice<Article> findArticles(int pageNumber, int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		return articleRepository.findByIdOrderByIdDesc(pageable);
	}
}
