package hiccreboot.backend.domains.article.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hiccreboot.backend.domains.article.domain.Article;
import hiccreboot.backend.domains.article.domain.ArticleGrade;
import hiccreboot.backend.domains.article.domain.BoardType;
import hiccreboot.backend.domains.member.domain.Member;

public interface ArticleRepository extends JpaRepository<Article, Long> {
	Page<Article> findAll(Pageable pageable);

	Page<Article> findAllByBoardTypeAndArticleGrade(BoardType boardType, ArticleGrade articleGrade, Pageable pageable);

	Page<Article> findAllByBoardType(BoardType boardType, Pageable pageable);

	List<Article> findAllByMember(Member member);

	@Query("select a from Article a where a.memberName like concat('%', :name, '%') and a.boardType=:boardType and a.articleGrade=:articleGrade")
	Page<Article> findAllByMemberNameAndBoardTypeAndArticleGrade(String name, BoardType boardType,
		ArticleGrade articleGrade,
		Pageable pageable);

	@Query("select a from Article a where a.memberName like concat('%', :name, '%') and a.boardType=:boardType")
	Page<Article> findAllByMemberNameAndBoardType(String name, BoardType boardType, Pageable pageable);

	Page<Article> findAllByMember(Member member, Pageable pageable);

	Page<Article> findAllBySubjectContainingAndBoardTypeAndArticleGrade(String subject, BoardType boardType,
		ArticleGrade articleGrade,
		Pageable pageable);

	Page<Article> findAllBySubjectContainingAndBoardType(String subject, BoardType boardType, Pageable pageable);

}
