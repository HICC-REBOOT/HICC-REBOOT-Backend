package hiccreboot.backend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BoardType boardType;

    @OneToMany(mappedBy = "article")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "article")
    private List<Appendix> appendices = new ArrayList<>();

    @Builder
    private Article(Member member, String subject, String content, BoardType boardType) {
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

    //연관 관계 메서드
    public void addAppendix(Appendix appendix) {
        this.appendices.add(appendix);
        appendix.setArticle(this);
    }
}
