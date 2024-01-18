package hiccreboot.backend.repository.Article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import hiccreboot.backend.domain.Article;
import hiccreboot.backend.domain.BoardType;

public interface ArticleRepository extends JpaRepository<Article, Long> {
	Page<Article> findAll(Pageable pageable);

	Page<Article> findAllByBoardType(Pageable pageable, BoardType boardType);

	Page<Article> findByMember_NameAndBoardType(String name, BoardType boardType, Pageable pageable);

	Page<Article> findBySubjectContainingAndBoardType(String subject, BoardType boardType, Pageable pageable);
}
