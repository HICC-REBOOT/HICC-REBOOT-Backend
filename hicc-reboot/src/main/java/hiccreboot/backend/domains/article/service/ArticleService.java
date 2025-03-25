package hiccreboot.backend.domains.article.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.common.exception.AccessForbiddenException;
import hiccreboot.backend.common.exception.ArticleNotFoundException;
import hiccreboot.backend.common.exception.BoardTypeNotFoundException;
import hiccreboot.backend.common.exception.ImageCountTooLarge;
import hiccreboot.backend.common.exception.MemberNotFoundException;
import hiccreboot.backend.domains.article.domain.Article;
import hiccreboot.backend.domains.article.domain.ArticleGrade;
import hiccreboot.backend.domains.article.domain.BoardTypeEntity;
import hiccreboot.backend.domains.article.dto.request.ArticleRequest;
import hiccreboot.backend.domains.article.dto.response.ArticleListResponse;
import hiccreboot.backend.domains.article.dto.response.ArticleResponse;
import hiccreboot.backend.domains.article.repository.ArticleRepository;
import hiccreboot.backend.domains.article.repository.BoardTypeRepository;
import hiccreboot.backend.domains.image.domain.Image;
import hiccreboot.backend.domains.image.service.S3Service;
import hiccreboot.backend.domains.member.domain.Grade;
import hiccreboot.backend.domains.member.domain.Member;
import hiccreboot.backend.domains.member.repository.MemberRepository;
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
	private final int BOARD_TYPE_ALL_INDEX = -1; // 전체 게시글 조회

	private final ArticleRepository articleRepository;
	private final MemberRepository memberRepository;
	private final S3Service s3Service;
	private final BoardTypeRepository boardTypeRepository;

	public Page<Article> findArticles(int pageNumber, int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id").descending());
		return articleRepository.findAll(pageable);
	}

	// 게시글 목록 조회
	public Page<ArticleListResponse> makeArticles(
		int pageNumber,
		int pageSize,
		Long boardTypeId,
		ArticleGrade articleGrade,
		String findBy,
		String search
	) {
		PageRequest pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id").descending());

		return findArticlesByFilter(
			pageable,
			boardTypeId,
			articleGrade,
			findBy,
			search
		).map(ArticleListResponse::create);
	}

	// 각 필터 조건으로 조회
	private Page<Article> findArticlesByFilter(
		Pageable pageable,
		Long boardTypeId,
		ArticleGrade articleGrade,
		String findBy,
		String search
	) {
		findBy = findBy.toUpperCase();

		// 1. 제목으로 검색, 전체 게시글, 운영진 글만 조회
		if (findBy.equals(FIND_BY_SUBJECT) && boardTypeId == BOARD_TYPE_ALL_INDEX
			&& articleGrade == ArticleGrade.EXECUTIVE) {
			return articleRepository.findAllBySubjectContainingAndArticleGrade(search, articleGrade, pageable);
		}

		// 2. 제목으로 검색, 전체 게시글, 모든 글 조회
		if (findBy.equals(FIND_BY_SUBJECT) && boardTypeId == BOARD_TYPE_ALL_INDEX) {
			return articleRepository.findAllBySubjectContaining(search, pageable);
		}

		// 3. 제목으로 검색, 특정 게시판, 운영진 글만 조회
		if (findBy.equals(FIND_BY_SUBJECT) && articleGrade == ArticleGrade.EXECUTIVE) {
			return articleRepository.findAllBySubjectContainingAndBoardType_IdAndArticleGrade(search, boardTypeId,
				articleGrade, pageable);

		}

		// 4. 제목으로 검색, 특정 게시판, 모든 글 조회
		if (findBy.equals(FIND_BY_SUBJECT)) {
			return articleRepository.findAllBySubjectContainingAndBoardType_Id(search, boardTypeId, pageable);
		}

		// 5. 작성자 이름으로 검색, 전체 게시글, 운영진 글만 조회
		if (findBy.equals(FIND_BY_MEMBER_NAME) && boardTypeId == BOARD_TYPE_ALL_INDEX
			&& articleGrade == ArticleGrade.EXECUTIVE) {
			return articleRepository.findAllByMemberNameAndArticleGrade(search, articleGrade, pageable);
		}

		// 6. 작성자 이름으로 검색, 전체 게시글, 모든 글 조회
		if (findBy.equals(FIND_BY_MEMBER_NAME) && boardTypeId == BOARD_TYPE_ALL_INDEX) {
			return articleRepository.findAllByMemberName(search, pageable);
		}

		// 7. 작성자 이름으로 검색, 특정 게시판, 운영진 글만 조회
		if (findBy.equals(FIND_BY_MEMBER_NAME) && articleGrade == ArticleGrade.EXECUTIVE) {
			return articleRepository.findAllByMemberNameAndBoardType_IdAndArticleGrade(search, boardTypeId,
				articleGrade, pageable);
		}

		// 8. 작성자 이름으로 검색, 특정 게시판, 모든 글 조회
		if (findBy.equals(FIND_BY_MEMBER_NAME)) {
			return articleRepository.findAllByMemberNameAndBoardType_Id(search, boardTypeId, pageable);
		}

		// 9. 검색어로 검색x, 전체 게시글, 운영진 글만 조회
		if (boardTypeId == BOARD_TYPE_ALL_INDEX && articleGrade == ArticleGrade.EXECUTIVE) {
			return articleRepository.findAllByArticleGrade(articleGrade, pageable);
		}

		// 10. 검색어로 검색x, 전체 게시글, 모든 글 조회
		if (boardTypeId == BOARD_TYPE_ALL_INDEX) {
			return articleRepository.findAll(pageable);
		}

		// 11. 검색어로 검색x, 특정 게시판, 운영진 글만 조회
		if (articleGrade == ArticleGrade.EXECUTIVE) {
			return articleRepository.findAllByBoardType_IdAndArticleGrade(boardTypeId, articleGrade, pageable);
		}

		// 12. 검색어로 검색x, 특정 게시판, 모든 글 조회
		return articleRepository.findAllByBoardType_Id(boardTypeId, pageable);
	}

	private Optional<Article> findArticle(Long id) {
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
		Member member = memberRepository.findByStudentNumber(studentNumber)
			.orElseThrow(() -> MemberNotFoundException.EXCEPTION);

		checkImageSize(articleRequest.getImages().size());

		// 게시판 타입 조회
		BoardTypeEntity boardType = boardTypeRepository.findById(articleRequest.getBoardTypeId())
			.orElseThrow(() -> BoardTypeNotFoundException.EXCEPTION);

		Article article = Article.create(
			member,
			makeArticleGradeByMemberGrade(member.getGrade()),
			articleRequest.getSubject(),
			articleRequest.getContent(),
			boardType
		);

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
		BoardTypeEntity boardType = boardTypeRepository.findById(articleRequest.getBoardTypeId())
			.orElseThrow(() -> BoardTypeNotFoundException.EXCEPTION);

		checkUpdateAuthority(member, article);
		checkImageSize(articleRequest.getImages().size());

		article.updateSubject(articleRequest.getSubject());
		article.updateContent(articleRequest.getContent());
		article.updateBoardType(boardType);

		// 기존 이미지 중에 게시글에서 제외된 것은 삭제하지 않고, 오직 추가된 것만 추가로 저장한다. 제외된 것은 게시글 삭제시에만 전부 삭제된다.
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
