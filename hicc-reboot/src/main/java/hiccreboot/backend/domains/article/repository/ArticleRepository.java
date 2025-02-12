package hiccreboot.backend.domains.article.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hiccreboot.backend.domains.article.domain.Article;
import hiccreboot.backend.domains.article.domain.ArticleGrade;
import hiccreboot.backend.domains.member.domain.Member;

public interface ArticleRepository extends JpaRepository<Article, Long> {
	Page<Article> findAll(Pageable pageable);

	List<Article> findAllByMember(Member member);

	Page<Article> findAllByMember(Member member, Pageable pageable);

	// 회원 이름으로 운영진 글 검색
	@Query("select a from Article a where a.memberName like concat('%', :name, '%') and a.articleGrade=:articleGrade")
	Page<Article> findAllByMemberNameAndArticleGrade(String name, ArticleGrade articleGrade, Pageable pageable);

	// 회원 이름으로 글 검색
	@Query("select a from Article a where a.memberName like concat('%', :name, '%')")
	Page<Article> findAllByMemberName(String name, Pageable pageable);

	// 회원 이름으로, 특정 게시판의 운영진 글 검색
	@Query("select a from Article a where a.memberName like concat('%', :name, '%') and a.boardType.id=:boardTypeId and a.articleGrade=:articleGrade")
	Page<Article> findAllByMemberNameAndBoardType_IdAndArticleGrade(
		String name,
		Long boardTypeId,
		ArticleGrade articleGrade,
		Pageable pageable
	);

	// 회원 이름으로, 특정 게시판의 글 검색
	@Query("select a from Article a where a.memberName like concat('%', :search, '%') and a.boardType.id=:boardTypeId")
	Page<Article> findAllByMemberNameAndBoardType_Id(String search, Long boardTypeId, Pageable pageable);

	// 전체 게시판에서 이름으로 운영진 글 검색
	Page<Article> findAllBySubjectContainingAndArticleGrade(
		String subject,
		ArticleGrade articleGrade,
		Pageable pageable
	);

	// 특정 게시판에서 미름으로 글 검색
	Page<Article> findAllBySubjectContainingAndBoardType_Id(String subject, Long boardTypeId, Pageable pageable);

	// 특정 게시판에서 이름으로 운영진 글 검색
	Page<Article> findAllBySubjectContainingAndBoardType_IdAndArticleGrade(
		String subject,
		Long boardTypeId,
		ArticleGrade articleGrade,
		Pageable pageable
	);

	// 전체 게시판에서 이름으로 글 검색
	Page<Article> findAllBySubjectContaining(String subject, Pageable pageable);

	Page<Article> findAllByArticleGrade(ArticleGrade articleGrade, Pageable pageable);

	Page<Article> findAllByBoardType_IdAndArticleGrade(Long boardTypeId, ArticleGrade articleGrade, Pageable pageable);

	Page<Article> findAllByBoardType_Id(Long boardTypeId, Pageable pageable);
}
