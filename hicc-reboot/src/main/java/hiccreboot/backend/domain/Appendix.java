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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Appendix {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "APPENDIX_ID")
	private Long id;

	@Column(nullable = false)
	private String fileName;

	@Column(nullable = false)
	private String fileNameExtention;

	@Column(nullable = false)
	private String url;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ARTICLE_ID")
	private Article article;

	@Builder(access = AccessLevel.PRIVATE)
	private Appendix(String fileName, String fileNameExtention, String url, Article article) {
		this.fileName = fileName;
		this.url = url;
		this.fileNameExtention = fileNameExtention;
		changeArticle(article);
	}

	public static Appendix createAppendix(String fileName, String fileNameExtention, String url, Article article) {
		return Appendix.builder()
			.fileName(fileName)
			.fileNameExtention(fileNameExtention)
			.url(url)
			.article(article)
			.build();
	}

	public void changeArticle(Article article) {
		this.article = article;
		article.getAppendices().add(this);
	}
}
