package hiccreboot.backend.common.dto.S3;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ImageRequest {

	private String fileName;
	private String fileNameExtension;
	private String key;
	private String url;
}
