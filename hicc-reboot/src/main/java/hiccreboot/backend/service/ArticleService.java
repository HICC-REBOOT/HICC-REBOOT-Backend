package hiccreboot.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hiccreboot.backend.common.dto.Article.ArticleListResponse;
import hiccreboot.backend.common.dto.Article.ArticleResponse;
import hiccreboot.backend.common.dto.BaseResponse;
import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.common.dto.S3.ImageRequest;
import hiccreboot.backend.common.exception.ArticleNotFoundException;
import hiccreboot.backend.common.exception.MemberNotFoundException;
import hiccreboot.backend.domain.Article;
import hiccreboot.backend.domain.BoardType;
import hiccreboot.backend.domain.Image;
import hiccreboot.backend.domain.Member;
import hiccreboot.backend.repository.Article.ArticleRepository;
import hiccreboot.backend.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {

	private final ArticleRepository articleRepository;
	private final MemberRepository memberRepository;

	public Page<Article> findArticles(int pageNumber, int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		return articleRepository.findAll(pageable);
	}

	private List<Article> findArticleBySortAndBoardType(int pageNumber, int pageSize, BoardType boardType, String sort,
		String search) {
		if (sort.equals("article")) {
			return findArticlesByBoardType(pageNumber, pageSize, boardType).getContent();
		}
		if (sort.equals("member")) {
			return findArticlesByMemberNameAndBoardType(pageNumber, pageSize, boardType, search).getContent();
		}
		if (sort.equals("subject")) {
			return findArticlesBySubjectAndBoardType(pageNumber, pageSize, boardType, search).getContent();
		}

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

	public DataResponse<List<ArticleListResponse>> makeArticles(int pageNumber, int pageSize, BoardType boardType,
		String sort,
		String search) {
		List<Article> articles = findArticleBySortAndBoardType(pageNumber, pageSize, boardType, sort,
			search);

		if (articles.isEmpty()) {
			throw ArticleNotFoundException.EXCEPTION;
		}

		List<ArticleListResponse> articleResponses = new ArrayList<>();
		articles.stream()
			.forEach(article -> articleResponses.add(ArticleListResponse.create(article)));

		return DataResponse.ok(articleResponses);
	}

	public Optional<Article> findArticle(Long id) {
		return articleRepository.findById(id);
	}

	public DataResponse<ArticleResponse> makeArticle(Long id) {
		Article article = findArticle(id).orElseThrow(() -> ArticleNotFoundException.EXCEPTION);

		return DataResponse.ok(ArticleResponse.create(article));
	}

	@Transactional
	public Article saveArticle(String studentNumber, String subject, String content, BoardType boardType,
		List<ImageRequest> imageRequests) {
		Member member = memberRepository.findByStudentNumber(studentNumber).orElseThrow(() ->
			MemberNotFoundException.EXCEPTION);
		Article article = Article.createArticle(member, subject, content, boardType);

		for (ImageRequest imageRequest : imageRequests) {
			String fileName = imageRequest.getFileName();
			String fileNameExtention = imageRequest.getFileNameExtention();
			String key = imageRequest.getKey();
			String url = imageRequest.getUrl();
			Image.createImage(fileName, fileNameExtention, key, url, article);
		}

		return articleRepository.save(article);
	}

	@Transactional
	public BaseResponse updateArticle(Long id, String subject, String content, BoardType boardType,
		List<String> appendices) {
		Article article = findArticle(id).orElseThrow(() -> ArticleNotFoundException.EXCEPTION);

		article.updateSubject(subject);
		article.updateContent(content);
		article.updateBoardType(boardType);

		List<Image> articleAppendices = article.getAppendices();

		// update 구현
		return DataResponse.noContent();
	}

	@Transactional
	public void deleteArticle(Long id) {
		articleRepository.deleteById(id);
	}

}
