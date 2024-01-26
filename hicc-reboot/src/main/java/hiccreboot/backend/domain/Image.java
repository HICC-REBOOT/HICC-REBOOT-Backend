package hiccreboot.backend.domain;

import hiccreboot.backend.common.dto.S3.ImageRequest;
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
public class Image {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "IMAGE_ID")
	private Long id;

	@Column(nullable = false)
	private String fileName;

	@Column(nullable = false)
	private String fileNameExtension;

	@Column(nullable = false, name = "IMAGE_KEY")
	private String key;

	@Column(nullable = false)
	private String url;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ARTICLE_ID")
	private Article article;

	@Builder(access = AccessLevel.PRIVATE)
	private Image(String fileName, String fileNameExtension, String key, String url, Article article) {
		this.fileName = fileName;
		this.fileNameExtension = fileNameExtension.toUpperCase();
		this.key = key;
		this.url = url;
		changeArticle(article);
	}

	public static Image createImage(String fileName, String fileNameExtension, String key, String url,
		Article article) {
		return Image.builder()
			.fileName(fileName)
			.fileNameExtension(fileNameExtension)
			.key(key)
			.url(url)
			.article(article)
			.build();
	}

	public static Image createImage(ImageRequest imageRequest, Article article) {
		return Image.builder()
			.fileName(imageRequest.getFileName())
			.fileNameExtension(imageRequest.getFileNameExtension())
			.key(imageRequest.getKey())
			.url(imageRequest.getUrl())
			.article(article)
			.build();
	}

	public void changeArticle(Article article) {
		this.article = article;
		article.getImages().add(this);
	}
}
