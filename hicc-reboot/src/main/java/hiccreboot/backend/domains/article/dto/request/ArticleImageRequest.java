package hiccreboot.backend.domains.article.dto.request;

import lombok.Getter;

@Getter
public class ArticleImageRequest {

	private String fileName;
	private String fileNameExtension;
	private String key;
}
