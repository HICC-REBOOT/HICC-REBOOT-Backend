package hiccreboot.backend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ARTICLE_ID")
    private Article article;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private CommentType type;

    @Column(nullable = true)
    private Long parentCommentId;

    @Builder
    private Comment(Long id, Article article, String content, LocalDateTime date, CommentType type, Long parentCommentId) {
        this.id = id;
        this.article = article;
        this.content = content;
        this.date = date;
        this.type = type;
        this.parentCommentId = parentCommentId;
    }

    public Comment createComment(Article article, String content, LocalDateTime date, CommentType type) {
        return Comment.builder().article(article).content(content).date(date).type(type).build();
    }

    public Comment createReplyToComment(Article article, String content, LocalDateTime date, CommentType type, Long parentCommentId) {
        return Comment.builder().article(article).content(content).date(date).type(type).parentCommentId(parentCommentId).build();
    }
}
