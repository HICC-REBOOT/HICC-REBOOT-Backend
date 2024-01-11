package hiccreboot.backend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Appendix {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "APPENDIX_ID")
    private Long id;

    @Column(nullable = false)
    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appendix", insertable = false, updatable = false)
    @Column(nullable = false)
    private Article article;

    @Builder
    private Appendix(String path, Article article) {
        this.path = path;
        this.article = article;
    }

    public Appendix createAppendix(String path) {
        return Appendix.builder()
                .path(path)
                .build();
    }

    public void setArticle(Article article) {
        this.article = article;
    }
}
