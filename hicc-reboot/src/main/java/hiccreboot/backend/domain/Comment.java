package hiccreboot.backend.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ARTICLE_ID")
	private Article article;

	@Column(nullable = false)
	private String content;

	@Builder(access = AccessLevel.PRIVATE)
	private Comment(Long parentCommentId, LocalDateTime date, Member member, Article article, String content) {
		this.parentCommentId = parentCommentId;
		this.date = date;
		addMember(member);
		addArticle(article);
		this.content = content;
	}

	public static Comment createParentComment(Member member, Article article, String content) {
		return Comment.builder()
			.member(member)
			.article(article)
			.date(LocalDateTime.now())
			.content(content)
			.build();
	}

	public static Comment createChildComment(Long parentCommentId, Member member, Article article, String content) {
		return Comment.builder()
			.parentCommentId(parentCommentId)
			.member(member)
			.article(article)
			.date(LocalDateTime.now())
			.content(content)
			.build();
	}

	//연관 관계 메서드
	private void addMember(Member member) {
		this.member = member;
		member.getComments().add(this);
	}

	private void addArticle(Article article) {
		this.article = article;
		article.getComments().add(this);
	}
}
