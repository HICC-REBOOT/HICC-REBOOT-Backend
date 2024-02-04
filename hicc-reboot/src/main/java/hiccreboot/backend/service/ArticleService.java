package hiccreboot.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hiccreboot.backend.common.dto.Article.ArticleListResponse;
import hiccreboot.backend.common.dto.Article.ArticleRequest;
import hiccreboot.backend.common.dto.Article.ArticleResponse;
import hiccreboot.backend.common.dto.BaseResponse;
import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.common.exception.AccessForbiddenException;
import hiccreboot.backend.common.exception.ArticleNotFoundException;
import hiccreboot.backend.common.exception.ImageCountTooLarge;
import hiccreboot.backend.common.exception.MemberNotFoundException;
import hiccreboot.backend.domain.Article;
import hiccreboot.backend.domain.ArticleGrade;
import hiccreboot.backend.domain.BoardType;
import hiccreboot.backend.domain.Grade;
import hiccreboot.backend.domain.Image;
import hiccreboot.backend.domain.Member;
import hiccreboot.backend.repository.Article.ArticleRepository;
import hiccreboot.backend.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {
	private final String FIND_BY_MEMBER_NAME = "MEMBER";
	private final String FIND_BY_SUBJECT = "SUBJECT";
	private final int IMAGE_COUNT_LIMIT = 10;

	private final ArticleRepository articleRepository;
	private final MemberRepository memberRepository;
	private final S3Service s3Service;

	public Page<Article> findArticles(int pageNumber, int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id").descending());
		return articleRepository.findAll(pageable);
	}

	private Page<Article> findArticlesByFindBy(
		Pageable pageable,
		BoardType boardType,
		ArticleGrade articleGrade,
		String findBy,
		String search) {
		findBy = findBy.toUpperCase();
		if (findBy.equals(FIND_BY_SUBJECT)) {
			return findArticlesBySubjectAndBoardTypeAndArticleGrade(pageable, boardType, articleGrade, search);
		} else if (findBy.equals(FIND_BY_MEMBER_NAME)) {
			return findArticlesByMemberNameAndBoardTypeAndArticleGrade(pageable, boardType, articleGrade,
				search);
		} else {
			return findArticlesByBoardTypeAndArticleGrade(pageable, boardType, articleGrade);
		}
	}

	private Page<Article> findArticlesByBoardTypeAndArticleGrade(Pageable pageable, BoardType boardType,
		ArticleGrade articleGrade) {
		if (articleGrade == ArticleGrade.EXECUTIVE) {
			return articleRepository.findAllByBoardTypeAndArticleGrade(boardType, articleGrade, pageable);
		}
		return articleRepository.findAllByBoardType(boardType, pageable);
	}

	private Page<Article> findArticlesByMemberNameAndBoardTypeAndArticleGrade(Pageable pageable,
		BoardType boardType,
		ArticleGrade articleGrade,
		String search) {
		if (articleGrade == ArticleGrade.EXECUTIVE) {
			return articleRepository.findAllByMemberNameAndBoardTypeAndArticleGrade(search, boardType, articleGrade,
				pageable);
		}
		return articleRepository.findAllByMemberNameAndBoardType(search, boardType, pageable);
	}

	private Page<Article> findArticlesBySubjectAndBoardTypeAndArticleGrade(Pageable pageable, BoardType boardType,
		ArticleGrade articleGrade,
		String search) {
		if (articleGrade == ArticleGrade.EXECUTIVE) {
			return articleRepository.findAllBySubjectContainingAndBoardTypeAndArticleGrade(search, boardType,
				articleGrade,
				pageable);
		}
		return articleRepository.findAllBySubjectContainingAndBoardType(search, boardType, pageable);
	}

	public BaseResponse makeArticles(int pageNumber, int pageSize, BoardType boardType,
		ArticleGrade articleGrade,
		String findBy,
		String search) {
		PageRequest pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id").descending());

		Page<ArticleListResponse> articles = findArticlesByFindBy(pageable,
			boardType, articleGrade, findBy,
			search).map(ArticleListResponse::create);

		return DataResponse.ok(articles);
	}

	private Optional<Article> findArticle(Long id) {
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

		checkImageSize(articleRequest.getImages().size());

		Article article = Article.create(member, makeArticleGradeByMemberGrade(member.getGrade()), articleRequest);

		articleRequest.getImages()
			.forEach(imageRequest -> Image.createImage(imageRequest, s3Service.getUrl(imageRequest.getKey()), article));

		return articleRepository.save(article);
	}

	private ArticleGrade makeArticleGradeByMemberGrade(Grade grade) {
		if (grade == Grade.EXECUTIVE || grade == Grade.PRESIDENT) {
			return ArticleGrade.EXECUTIVE;
		}
		if (grade == Grade.NORMAL) {
			return ArticleGrade.NORMAL;
		}
		throw AccessForbiddenException.EXCEPTION;
	}

	@Transactional
	public Article updateArticle(Long id, ArticleRequest articleRequest, String studentNumber) {
		Member member = memberRepository.findByStudentNumber(studentNumber)
			.orElseThrow(() -> MemberNotFoundException.EXCEPTION);
		Article article = findArticle(id).orElseThrow(() -> ArticleNotFoundException.EXCEPTION);

		checkUpdateAuthority(member, article);
		checkImageSize(articleRequest.getImages().size());

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

	private void checkUpdateAuthority(Member member, Article article) {
		if (member != article.getMember()) {
			throw AccessForbiddenException.EXCEPTION;
		}
	}

	@Transactional
	public void deleteArticle(Long id, String studentNumber) {
		Member member = memberRepository.findByStudentNumber(studentNumber)
			.orElseThrow(() -> MemberNotFoundException.EXCEPTION);
		Article article = articleRepository.findById(id)
			.orElseThrow(() -> ArticleNotFoundException.EXCEPTION);

		checkDeleteAuthority(member, article);

		//S3 image 제거
		article.getImages()
			.forEach(image -> s3Service.deleteImage(image.getKey()));

		articleRepository.deleteById(id);
	}

	private void checkDeleteAuthority(Member member, Article article) {
		if (member.getGrade() == Grade.PRESIDENT || member.getGrade() == Grade.EXECUTIVE) {
			return;
		}
		if (member == article.getMember()) {
			return;
		}
		throw AccessForbiddenException.EXCEPTION;
	}

	private void checkImageSize(int size) {
		if (size > IMAGE_COUNT_LIMIT) {
			throw ImageCountTooLarge.EXCEPTION;
		}
	}
}
