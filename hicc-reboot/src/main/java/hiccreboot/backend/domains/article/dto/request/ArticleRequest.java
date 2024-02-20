package hiccreboot.backend.domains.article.dto.request;

import java.util.List;

import hiccreboot.backend.domains.article.domain.BoardType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ArticleRequest {
	@Valid
	@NotNull
	private List<ArticleImageRequest> images;
	@NotNull
	private BoardType board;
	@NotBlank
	private String subject;
	@NotBlank
	private String content;

}
