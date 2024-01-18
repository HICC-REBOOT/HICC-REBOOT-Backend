package hiccreboot.backend.domain;

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
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Appendix {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "APPENDIX_ID")
	private Long id;

	@Column(nullable = false)
	private String path;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ARTICLE_ID")
	private Article article;

	@Builder
	private Appendix(String path, Article article) {
		this.path = path;
		this.article = article;
	}

	public Appendix createAppendix(String path) {
		return Appendix.builder()
			.path(path)
			.build();
	}

	public void changeArticle(Article article) {
		this.article = article;
		article.getAppendices().add(this);
	}
}
