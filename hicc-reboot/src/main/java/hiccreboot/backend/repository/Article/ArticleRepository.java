package hiccreboot.backend.repository.Article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import hiccreboot.backend.domain.Article;
import hiccreboot.backend.domain.ArticleGrade;
import hiccreboot.backend.domain.BoardType;
import hiccreboot.backend.domain.Member;

public interface ArticleRepository extends JpaRepository<Article, Long> {
	Page<Article> findAll(Pageable pageable);

	Page<Article> findAllByBoardTypeAndArticleGrade(BoardType boardType, ArticleGrade articleGrade, Pageable pageable);

	Page<Article> findAllByMember_NameAndBoardTypeAndArticleGrade(String name, BoardType boardType,
		ArticleGrade articleGrade,
		Pageable pageable);

	Page<Article> findBySubjectContainingAndBoardType(String subject, BoardType boardType, Pageable pageable);

	Page<Article> findAllByMember(Member member, Pageable pageable);
	Page<Article> findAllBySubjectContainingAndBoardTypeAndArticleGrade(String subject, BoardType boardType,
		ArticleGrade articleGrade,
		Pageable pageable);

}
