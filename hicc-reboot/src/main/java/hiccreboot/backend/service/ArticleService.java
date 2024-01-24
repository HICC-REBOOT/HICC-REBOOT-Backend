package hiccreboot.backend.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hiccreboot.backend.common.dto.Article.ArticleListResponse;
import hiccreboot.backend.common.dto.Article.ArticleRequest;
import hiccreboot.backend.common.dto.Article.ArticleResponse;
import hiccreboot.backend.common.dto.BaseResponse;
import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.common.dto.S3.ImageRequest;
import hiccreboot.backend.common.exception.AccessForbiddenException;
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
	private final S3Service s3Service;

	public Page<Article> findArticles(int pageNumber, int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		return articleRepository.findAll(pageable);
	}

	private Page<Article> findArticleBySortAndBoardType(int pageNumber, int pageSize, BoardType boardType, String sort,
		String search) {
		if (sort.equals("article")) {
			return findArticlesByBoardType(pageNumber, pageSize, boardType);
		}
		if (sort.equals("member")) {
			return findArticlesByMemberNameAndBoardType(pageNumber, pageSize, boardType, search);
		}
		if (sort.equals("subject")) {
			return findArticlesBySubjectAndBoardType(pageNumber, pageSize, boardType, search);
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

	public DataResponse<Page<ArticleListResponse>> makeArticles(int pageNumber, int pageSize, BoardType boardType,
		String sort,
		String search) {
		Page<ArticleListResponse> articles = findArticleBySortAndBoardType(pageNumber, pageSize, boardType, sort,
			search).map(ArticleListResponse::create);

		if (articles.isEmpty()) {
			throw ArticleNotFoundException.EXCEPTION;
		}

		return DataResponse.ok(articles);
	}

	public Optional<Article> findArticle(Long id) {
		return articleRepository.findById(id);
	}

	public DataResponse<ArticleResponse> makeArticle(Long id, String studentNumber) {
		Member member = memberRepository.findByStudentNumber(studentNumber)
			.orElseThrow(() -> MemberNotFoundException.EXCEPTION);

		Article article = findArticle(id).orElseThrow(() -> ArticleNotFoundException.EXCEPTION);

		return DataResponse.ok(ArticleResponse.create(article, member == article.getMember()));
	}

	@Transactional
	public Article saveArticle(String studentNumber, ArticleRequest articleRequest) {
		Member member = memberRepository.findByStudentNumber(studentNumber).orElseThrow(() ->
			MemberNotFoundException.EXCEPTION);
		Article article = Article.createArticle(member, articleRequest.getSubject(), articleRequest.getContent(),
			articleRequest.getBoard());

		for (ImageRequest imageRequest : articleRequest.getImages()) {
			String fileName = imageRequest.getFileName();
			String fileNameExtension = imageRequest.getFileNameExtension();
			String key = imageRequest.getKey();
			String url = imageRequest.getUrl();
			Image.createImage(fileName, fileNameExtension, key, url, article);
		}

		return articleRepository.save(article);
	}

	@Transactional
	public BaseResponse updateArticle(Long id, ArticleRequest articleRequest) {
		Article article = findArticle(id).orElseThrow(() -> ArticleNotFoundException.EXCEPTION);

		article.updateSubject(articleRequest.getSubject());
		article.updateContent(articleRequest.getContent());
		article.updateBoardType(articleRequest.getBoard());
		article.getImages().clear();
		articleRequest.getImages().stream()
			.forEach(image -> Image.createImage(image.getFileName(), image.getFileNameExtension(), image.getKey(),
				image.getUrl(), article));

		return DataResponse.noContent();
	}

	@Transactional
	public void deleteArticle(Long id, String studentNumber) {
		Member member = memberRepository.findByStudentNumber(studentNumber)
			.orElseThrow(() -> MemberNotFoundException.EXCEPTION);
		Article article = articleRepository.findById(id).orElseThrow(() -> ArticleNotFoundException.EXCEPTION);

		if (member != article.getMember()) {
			throw AccessForbiddenException.EXCEPTION;
		}

		//S3 image 제거
		article.getImages()
			.forEach(image -> s3Service.deleteImage(image.getFileName()));

		articleRepository.deleteById(id);
	}

}
