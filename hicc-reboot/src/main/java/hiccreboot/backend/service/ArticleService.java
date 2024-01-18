package hiccreboot.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hiccreboot.backend.common.dto.Article.ArticleResponse;
import hiccreboot.backend.common.dto.Article.ArticlesResponse;
import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.common.exception.ArticleNotFoundException;
import hiccreboot.backend.domain.Appendix;
import hiccreboot.backend.domain.Article;
import hiccreboot.backend.domain.BoardType;
import hiccreboot.backend.domain.Member;
import hiccreboot.backend.repository.Article.ArticleRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleService {

	private final ArticleRepository articleRepository;

	public Page<Article> findArticles(int pageNumber, int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		return articleRepository.findAll(pageable);
	}

	private List<Article> findArticleBySortAndBoardType(int pageNumber, int pageSize, BoardType boardType, String sort,
		String search) {
		if (sort.equals("null")) {
			return findArticlesByBoardType(pageNumber, pageSize, boardType).getContent();
		}
		if (sort.equals("member")) {
			return findArticlesByMemberNameAndBoardType(pageNumber, pageSize, boardType, search).getContent();
		}
		if (sort.equals("subject")) {
			return findArticlesBySubjectAndBoardType(pageNumber, pageSize, boardType, search).getContent();
		}

		// 이 부분 sort 값이 없는 Exception으로 변경
		throw ArticleNotFoundException.EXCEPTION;
	}

	private Page<Article> findArticlesByBoardType(int pageNumber, int pageSize, BoardType boardType) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		return articleRepository.findAllByBoardType(pageable, boardType);
	}

	private Page<Article> findArticlesByMemberNameAndBoardType(int pageNumber, int pageSize, BoardType boardType,
		String search) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		return articleRepository.findByMember_NameAndBoardType(search, boardType, pageable);
	}

	private Page<Article> findArticlesBySubjectAndBoardType(int pageNumber, int pageSize, BoardType boardType,
		String search) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		return articleRepository.findBySubjectContainingAndBoardType(search, boardType, pageable);
	}

	public DataResponse<ArticlesResponse> makeArticles(int pageNumber, int pageSize, BoardType boardType, String sort,
		String search) {
		List<Article> articles = findArticleBySortAndBoardType(pageNumber, pageSize, boardType, sort,
			search);

		if (articles.isEmpty()) {
			throw ArticleNotFoundException.EXCEPTION;
		}

		ArticlesResponse articlesResponse = new ArticlesResponse();
		articles.stream()
			.forEach(article -> articlesResponse.add(
				ArticleResponse.createArticleResponse(
					article.getId(),
					article.getMember().getGrade(),
					article.getMember().getName(),
					article.getDate(),
					article.getBoardType(),
					article.getSubject())));

		return DataResponse.ok(articlesResponse);
	}

	public Optional<Article> findArticle(Long id) {
		return articleRepository.findById(id);
	}

	public DataResponse<ArticleResponse> makeArticle(Long id) {
		Article article = findArticle(id).orElseThrow(() -> ArticleNotFoundException.EXCEPTION);

		return DataResponse.ok(ArticleResponse.createArticleResponse(
			article.getId(),
			article.getMember().getGrade(),
			article.getMember().getName(),
			article.getDate(),
			!article.getAppendices().isEmpty(),
			article.getAppendices(),
			article.getBoardType(),
			article.getSubject(),
			article.getContent()));
	}

	@Transactional
	public Article saveArticle(Member member, String subject, String content, BoardType boardType,
		List<String> appendices) {
		Article article = Article.createArticle(subject, content, boardType, LocalDateTime.now());
		article.changeMember(member);
		appendices.stream().forEach(appendix -> article.addAppendix(appendix));

		return articleRepository.save(article);
	}

	@Transactional
	public Article updateArticle(Long id, String subject, String content, BoardType boardType,
		List<String> appendices) {
		Article article = findArticle(id).orElseThrow(() -> ArticleNotFoundException.EXCEPTION);

		article.updateSubject(subject);
		article.updateContent(content);
		article.updateBoardType(boardType);

		List<Appendix> articleAppendices = article.getAppendices();

		// update 구현
	}

	@Transactional
	public void deleteArticle(Long id) {
		articleRepository.deleteById(id);
	}

}
