package hiccreboot.backend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {

    @Id
    @Column(name = "ARTICLE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ARTICLE_ID")
    private Member member;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private String content;

    @Enumerated
    @Column(nullable = false)
    private BoardType boardType;

    @Builder
    private Article(Member member, String subject, String content, BoardType boardType) {
        this.id = id;
        this.member = member;
        this.subject = subject;
        this.content = content;
        this.boardType = boardType;
    }

    public Article createArticle(Member member, String subject, String content, BoardType boardType) {
        return Article.builder()
                .member(member)
                .subject(subject)
                .content(content)
                .boardType(boardType)
                .build();
    }

    public void updateSubject(String subject) {
        this.subject = subject;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateBoardType(BoardType boardType) {
        this.boardType = boardType;
    }
}
