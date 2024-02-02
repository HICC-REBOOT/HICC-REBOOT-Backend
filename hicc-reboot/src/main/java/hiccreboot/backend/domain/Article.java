package hiccreboot.backend.domain;

import static hiccreboot.backend.common.consts.MemberConstants.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import hiccreboot.backend.common.dto.Article.ArticleRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {

	@Id
	@Column(name = "ARTICLE_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEMBER_ID")
	private Member member;

	@Column(nullable = false)
	private String memberName;

	@Column(name = "ARTICLE_GRADE")
	@Enumerated(EnumType.STRING)
	private ArticleGrade articleGrade;

	@Column(nullable = false)
	private String subject;

	@Column(nullable = false)
	@Lob
	private String content;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private BoardType boardType;

	@Column(nullable = false)
	private LocalDateTime date;

	@OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments = new ArrayList<>();

	@OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Image> images = new ArrayList<>();

	private Article(Member member, String memberName, ArticleGrade articleGrade, String subject, String content,
		BoardType boardType,
		LocalDateTime date) {
		this.member = member;
		this.memberName = memberName;
		this.articleGrade = articleGrade;
		this.subject = subject;
		this.content = content;
		this.boardType = boardType;
		this.date = date;
	}

	public static Article create(Member member, ArticleGrade articleGrade, String subject, String content,
		BoardType boardType) {
		return new Article(member, member.getName(), articleGrade, subject, content, boardType, LocalDateTime.now());
	}

	public static Article create(Member member, ArticleGrade articleGrade, ArticleRequest articleRequest) {
		return new Article(member, member.getName(), articleGrade, articleRequest.getSubject(),
			articleRequest.getContent(),
			articleRequest.getBoard(), LocalDateTime.now());
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

	public void deleteArticleSoftly() {
		this.member = null;
		this.memberName = DELETED_MEMBER_NAME;
		this.articleGrade = ArticleGrade.NORMAL;
	}
}
