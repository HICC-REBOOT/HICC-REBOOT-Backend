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
public class ParentComment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "COMMENT_ID")
	private Long id;

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

	@Builder
	public ParentComment(Long id, LocalDateTime date, Member member, Article article, String content) {
		this.id = id;
		this.date = date;
		this.member = member;
		this.article = article;
		this.content = content;
	}

	public static ParentComment createParentComment(LocalDateTime date, String content) {
		return ParentComment.builder().date(date).content(content).build();
	}

	//연관 관계 메서드
	public void addMember(Member member) {
		this.member = member;
		member.getParentComments().add(this);
	}

	public void addArticle(Article article) {
		this.article = article;
		article.getParentComments().add(this);
	}
}
