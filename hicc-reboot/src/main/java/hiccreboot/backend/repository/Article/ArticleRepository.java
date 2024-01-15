package hiccreboot.backend.repository.Article;

import hiccreboot.backend.domain.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Slice<Article> findByIdOrderByIdDesc(Pageable pageable);

    Slice<Article> findByMember_Name(String name, Pageable pageable);

    Slice<Article> findBySubjectContaining(String subject, Pageable pageable);
}
