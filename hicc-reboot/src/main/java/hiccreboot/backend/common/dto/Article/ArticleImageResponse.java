package hiccreboot.backend.common.dto.Article;

import lombok.Getter;

@Getter
public class ArticleImageResponse {

	private final String fileName;
	private final String fileNameExtension;
	private final String key;
	private final String url;

	private ArticleImageResponse(String fileName, String fileNameExtension, String key, String url) {
		this.fileName = fileName;
		this.fileNameExtension = fileNameExtension;
		this.key = key;
		this.url = url;
	}

	public static ArticleImageResponse create(String fileName, String fileNameExtension, String key, String url) {
		return new ArticleImageResponse(fileName, fileNameExtension, key, url);
	}
}
