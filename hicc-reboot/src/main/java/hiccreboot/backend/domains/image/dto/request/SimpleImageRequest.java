package hiccreboot.backend.domains.image.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SimpleImageRequest {

	@NotBlank
	private String fileName;
	@NotBlank
	private String fileNameExtension;
}
