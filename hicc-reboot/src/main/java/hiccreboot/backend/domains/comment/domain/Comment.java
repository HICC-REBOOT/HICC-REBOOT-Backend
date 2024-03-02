package hiccreboot.backend.domains.comment.domain;

import hiccreboot.backend.domains.article.domain.Article;
import hiccreboot.backend.domains.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static hiccreboot.backend.common.consts.MemberConstants.DELETED_MEMBER_NAME;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    private final String BLANK_CONTENT = "댓글이 삭제되었습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private Long id;

    @Column(nullable = true)
    private Long parentCommentId;

    @Column(nullable = false)
    private Boolean isDeleted;

    @Column(nullable = false)
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Column(nullable = false)
    private String memberName;

    @Column(name = "COMMENT_GRADE")
    @Enumerated(EnumType.STRING)
    private CommentGrade commentGrade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ARTICLE_ID")
    private Article article;

    @Column(nullable = false, length = 250)
    private String content;

    @Builder(access = AccessLevel.PRIVATE)
    private Comment(Long parentCommentId, Boolean isDeleted, LocalDateTime date, Member member, String memberName,
                    CommentGrade commentGrade, Article article,
                    String content) {
        this.parentCommentId = parentCommentId;
        this.isDeleted = isDeleted;
        this.date = date;
        this.member = member;
        this.memberName = memberName;
        this.commentGrade = commentGrade;
        addArticle(article);
        this.content = content;
    }

    public static Comment createParentComment(Member member, CommentGrade commentGrade, Article article,
                                              String content) {
        return Comment.builder()
                .parentCommentId(-1L)
                .isDeleted(false)
                .member(member)
                .memberName(member.getName())
                .commentGrade(commentGrade)
                .article(article)
                .date(LocalDateTime.now())
                .content(content)
                .build();
    }

    public static Comment createChildComment(Long parentCommentId, Member member, CommentGrade commentGrade,
                                             Article article, String content) {
        return Comment.builder()
                .parentCommentId(parentCommentId)
                .isDeleted(false)
                .member(member)
                .memberName(member.getName())
                .commentGrade(commentGrade)
                .article(article)
                .date(LocalDateTime.now())
                .content(content)
                .build();
    }

    private void addArticle(Article article) {
        this.article = article;
        article.getComments().add(this);
    }

    public void deleteCommentSoftly() {
        this.member = null;
        this.memberName = DELETED_MEMBER_NAME;
        this.isDeleted = true;
        commentGrade = CommentGrade.NORMAL;
    }

    public void deleteCommentSoftlyWithContent() {
        this.member = null;
        this.memberName = DELETED_MEMBER_NAME;
        this.isDeleted = true;
        commentGrade = CommentGrade.NORMAL;
        content = BLANK_CONTENT;
    }
}
