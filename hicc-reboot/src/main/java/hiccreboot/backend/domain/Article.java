package hiccreboot.backend.domain;

import jakarta.persistence.*;
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
	private List<ParentComment> parentComments = new ArrayList<>();

	@OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ChildComment> childComments = new ArrayList<>();

	@OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Appendix> appendices = new ArrayList<>();

	private Article(String subject, String content, BoardType boardType, LocalDateTime date) {
		this.subject = subject;
		this.content = content;
		this.boardType = boardType;
		this.date = date;
	}
	private Article(Member member, String subject, String content, BoardType boardType, LocalDateTime date) {
		changeMember(member);
		this.subject = subject;
		this.content = content;
		this.boardType = boardType;
		this.date = date;
	}

	public static Article createArticle(String subject, String content, BoardType boardType, LocalDateTime date) {
		return new Article(subject, content, boardType, date);
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

	private void changeMember(Member member) {
		this.member = member;
		member.getArticles().add(this);
	}
}
