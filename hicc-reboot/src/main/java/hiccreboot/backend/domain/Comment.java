package hiccreboot.backend.domain;

import static hiccreboot.backend.common.consts.MemberConstants.*;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

	private final String BLANK_CONTENT = "";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "COMMENT_ID")
	private Long id;

	@Column(nullable = true)
	private Long parentCommentId;

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

	@Column(nullable = false)
	private String content;

	@Builder(access = AccessLevel.PRIVATE)
	private Comment(Long parentCommentId, LocalDateTime date, Member member, String memberName,
		CommentGrade commentGrade, Article article,
		String content) {
		this.parentCommentId = parentCommentId;
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
		commentGrade = CommentGrade.NORMAL;
	}

	public void deleteCommentSoftlyWithContent() {
		this.member = null;
		this.memberName = DELETED_MEMBER_NAME;
		commentGrade = CommentGrade.NORMAL;
		content = BLANK_CONTENT;
	}
}
