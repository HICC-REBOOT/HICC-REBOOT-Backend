package hiccreboot.backend.service;

import hiccreboot.backend.domain.Appendix;
import hiccreboot.backend.domain.Article;
import hiccreboot.backend.domain.BoardType;
import hiccreboot.backend.domain.Member;
import hiccreboot.backend.repository.Article.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    public Slice<Article> findArticles(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return articleRepository.findByIdOrderByIdDesc(pageable);
    }

    public Slice<Article> findArticlesByMemberName(int pageNumber, int pageSize, String search) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return articleRepository.findByMember_Name(search, pageable);
    }

    public Slice<Article> findArticlesBySubject(int pageNumber, int pageSize, String search) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return articleRepository.findBySubjectContaining(search, pageable);
    }

    public Optional<Article> findArticle(Long id) {
        return articleRepository.findById(id);
    }

    @Transactional
    public Article saveArticle(Member member, String subject, String content, BoardType boardType, LocalDateTime localDateTime, List<Appendix> appendices) {
        Article article = Article.createArticle(subject, content, boardType, localDateTime);
        article.changeMember(member);
        appendices.stream().forEach(appendix -> article.addAppendix(appendix));

        return articleRepository.save(article);
    }

    @Transactional
    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }
}
