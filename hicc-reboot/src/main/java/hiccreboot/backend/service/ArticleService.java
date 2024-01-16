package hiccreboot.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hiccreboot.backend.common.dto.Article.ArticleResponse;
import hiccreboot.backend.common.exception.ArticleNotFoundException;
import hiccreboot.backend.domain.Appendix;
import hiccreboot.backend.domain.Article;
import hiccreboot.backend.domain.BoardType;
import hiccreboot.backend.domain.Grade;
import hiccreboot.backend.domain.Member;
import hiccreboot.backend.repository.Article.ArticleRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleService {

	private final ArticleRepository articleRepository;

	public Slice<Article> findArticles(int pageNumber, int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		return articleRepository.findByIdOrderByIdDesc(pageable);
	}

	private Slice<Article> findArticlesByMemberName(int pageNumber, int pageSize, String search) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		return articleRepository.findByMember_Name(search, pageable);
	}

	private Slice<Article> findArticlesBySubject(int pageNumber, int pageSize, String search) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		return articleRepository.findBySubjectContaining(search, pageable);
	}

	public List<Article> findArticleBySort(int pageNumber, int pageSize, String sort, String search) {
		if (sort.equals("null")) {
			return findArticles(pageNumber, pageSize).getContent();
		}
		if (sort.equals("member")) {
			return findArticlesByMemberName(pageNumber, pageSize, search).getContent();
		}
		if (sort.equals("subject")) {
			return findArticlesBySubject(pageNumber, pageSize, search).getContent();
		}

		// 이 부분 sort 값이 없는 Exception으로 변경
		return new ArticleNotFoundException();
	}

	public Optional<Article> findArticle(Long id) {
		return articleRepository.findById(id);
	}

	@Transactional
	public Article saveArticle(Member member, String subject, String content, BoardType boardType,
		LocalDateTime localDateTime, List<Appendix> appendices) {
		Article article = Article.createArticle(subject, content, boardType, localDateTime);
		article.changeMember(member);
		appendices.stream().forEach(appendix -> article.addAppendix(appendix));

		return articleRepository.save(article);
	}

	@Transactional
	public void deleteArticle(Long id) {
		articleRepository.deleteById(id);
	}

	public Object makeArticleDTO(Article article, Grade grade, String name) {
		if (article.getAppendices().size() != 0) {
			return ArticleResponse.createArticleResponse(article.getId(), grade, name, article.getDate(), true,
				article.getAppendices(), article.getBoardType(), article.getSubject(), article.getContent());
		}
		return ArticleResponse.createArticleResponse(article.getId(), grade, name, article.getDate(), false,
			article.getBoardType(), article.getSubject(), article.getContent());
	}

}
