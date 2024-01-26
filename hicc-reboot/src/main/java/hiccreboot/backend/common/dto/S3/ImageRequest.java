package hiccreboot.backend.common.dto.S3;

import lombok.Getter;

@Getter
public class ImageRequest {

	private String fileName;
	private String fileNameExtension;
	private String key;
	private String url;
}
