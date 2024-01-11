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

    @ManyToOne
    @JoinColumn(name = "ARTICLE_ID", insertable = false, updatable = false)
    private Article article;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private CommentType commentType;

    @Column(nullable = true)
    private Long parentCommentId;

    @Builder
    private Comment(Long id, Article article, String content, LocalDateTime date, CommentType commentType, Long parentCommentId) {
        this.id = id;
        this.article = article;
        this.content = content;
        this.date = date;
        this.commentType = commentType;
        this.parentCommentId = parentCommentId;
    }

    public Comment createComment(Article article, String content, LocalDateTime date, CommentType commentType) {
        return Comment.builder().article(article).content(content).date(date).commentType(commentType).build();
    }

    public Comment createReplyToComment(Article article, String content, LocalDateTime date, CommentType commentType, Long parentCommentId) {
        return Comment.builder().article(article).content(content).date(date).commentType(commentType).parentCommentId(parentCommentId).build();
    }
}
