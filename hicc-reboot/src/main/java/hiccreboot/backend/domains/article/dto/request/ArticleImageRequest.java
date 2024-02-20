package hiccreboot.backend.domains.article.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ArticleImageRequest {

	@NotBlank
	private String fileName;
	@NotBlank
	private String fileNameExtension;
	@NotBlank
	private String key;
}
