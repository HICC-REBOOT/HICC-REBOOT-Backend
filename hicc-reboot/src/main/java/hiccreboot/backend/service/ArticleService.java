package hiccreboot.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import hiccreboot.backend.common.exception.ArticleNotFoundException;
import hiccreboot.backend.common.exception.MemberNotFoundException;
import hiccreboot.backend.common.exception.SortNotFoundException;
import hiccreboot.backend.domain.Article;
import hiccreboot.backend.domain.ArticleGrade;
import hiccreboot.backend.domain.BoardType;
import hiccreboot.backend.domain.Grade;
import hiccreboot.backend.domain.Image;
import hiccreboot.backend.domain.Member;
import hiccreboot.backend.repository.Article.ArticleRepository;
import hiccreboot.backend.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {
	private final String SORT_BY_ARTICLE = "ARTICLE";
	private final String SORT_BY_MEMBER_NAME = "MEMBER";
	private final String SORT_BY_GRADE = "GRADE";

	private final ArticleRepository articleRepository;
	private final MemberRepository memberRepository;
	private final S3Service s3Service;

	public Page<Article> findArticles(int pageNumber, int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		return articleRepository.findAll(pageable);
	}

	private Page<Article> findArticleBySortAndBoardTypeAndArticleGrade(int pageNumber, int pageSize,
		BoardType boardType, ArticleGrade articleGrade,
		String sort,
		String search) {
		sort = sort.toUpperCase();

		if (sort.equals("ARTICLE")) {
			return findArticlesByBoardTypeAndArticleGrade(pageNumber, pageSize, boardType, articleGrade);
		}
		if (sort.equals("MEMBER")) {
			return findArticlesByMemberNameAndBoardTypeAndArticleGrade(pageNumber, pageSize, boardType, articleGrade,
				search);
		}
		if (sort.equals("SUBJECT")) {
			return findArticlesBySubjectAndBoardType(pageNumber, pageSize, boardType, articleGrade, search);
		}

		throw SortNotFoundException.EXCEPTION;
	}

	private Page<Article> findArticlesByBoardTypeAndArticleGrade(int pageNumber, int pageSize, BoardType boardType,
		ArticleGrade articleGrade) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		return articleRepository.findAllByBoardTypeAndArticleGrade(boardType, articleGrade, pageable);
	}

	private Page<Article> findArticlesByMemberNameAndBoardTypeAndArticleGrade(int pageNumber, int pageSize,
		BoardType boardType,
		ArticleGrade articleGrade,
		String search) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		return articleRepository.findAllByMember_NameAndBoardTypeAndArticleGrade(search, boardType, articleGrade,
			pageable);
	}

	private Page<Article> findArticlesBySubjectAndBoardType(int pageNumber, int pageSize, BoardType boardType,
		ArticleGrade articleGrade,
		String search) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		return articleRepository.findAllBySubjectContainingAndBoardTypeAndArticleGrade(search, boardType, articleGrade,
			pageable);
	}

	public BaseResponse makeArticles(int pageNumber, int pageSize, BoardType boardType,
		ArticleGrade articleGrade,
		String sort,
		String search) {
		Page<ArticleListResponse> articles = findArticleBySortAndBoardTypeAndArticleGrade(pageNumber, pageSize,
			boardType, articleGrade, sort,
			search).map(ArticleListResponse::create);

		if (articles.isEmpty()) {
			throw ArticleNotFoundException.EXCEPTION;
		}

		return DataResponse.ok(articles);
	}

	public Optional<Article> findArticle(Long id) {
		return articleRepository.findById(id);
	}

	public BaseResponse makeArticle(Long id, String studentNumber) {
		Member member = memberRepository.findByStudentNumber(studentNumber)
			.orElseThrow(() -> MemberNotFoundException.EXCEPTION);

		Article article = findArticle(id).orElseThrow(() -> ArticleNotFoundException.EXCEPTION);

		return DataResponse.ok(ArticleResponse.create(article, member == article.getMember()));
	}

	@Transactional
	public Article saveArticle(String studentNumber, ArticleRequest articleRequest) {
		Member member = memberRepository.findByStudentNumber(studentNumber).orElseThrow(() ->
			MemberNotFoundException.EXCEPTION);

		Article article = Article.create(member, makeArticleGradeByMemberGrade(member.getGrade()), articleRequest);

		articleRequest.getImages()
			.forEach(imageRequest -> Image.createImage(imageRequest, s3Service.getUrl(imageRequest.getKey()), article));

		return articleRepository.save(article);
	}

	public ArticleGrade makeArticleGradeByMemberGrade(Grade grade) {
		if (grade == Grade.EXECUTIVE || grade == Grade.PRESIDENT) {
			return ArticleGrade.EXECUTIVE;
		}
		return ArticleGrade.NORMAL;
	}

	@Transactional
	public Article updateArticle(Long id, ArticleRequest articleRequest) {
		Article article = findArticle(id).orElseThrow(() -> ArticleNotFoundException.EXCEPTION);

		article.updateSubject(articleRequest.getSubject());
		article.updateContent(articleRequest.getContent());
		article.updateBoardType(articleRequest.getBoard());

		// image 변경
		List<String> oldKeys = article.getImages().stream()
			.map(image -> image.getKey()).collect(Collectors.toList());
		List<String> newKeys = articleRequest.getImages().stream()
			.map(image -> image.getKey()).collect(Collectors.toList());

		List<String> deleteKeys = oldKeys.stream()
			.filter(oldKey -> !newKeys.contains(oldKey))
			.collect(Collectors.toList());

		//s3 image 삭제
		deleteKeys.stream()
			.forEach(key -> s3Service.deleteImage(key));

		article.getImages().clear();
		articleRequest.getImages().stream()
			.forEach(image -> Image.createImage(image.getFileName(), image.getFileNameExtension(), image.getKey(),
				s3Service.getUrl(image.getKey()), article));

		return article;
	}

	@Transactional
	public void deleteArticle(Long id, String studentNumber) {
		Member member = memberRepository.findByStudentNumber(studentNumber)
			.orElseThrow(() -> MemberNotFoundException.EXCEPTION);
		Article article = articleRepository.findById(id)
			.filter(foundArticle -> foundArticle.getMember() == member)
			.orElseThrow(() -> ArticleNotFoundException.EXCEPTION);

		//S3 image 제거
		article.getImages()
			.forEach(image -> s3Service.deleteImage(image.getKey()));

		articleRepository.deleteById(id);
	}

}
