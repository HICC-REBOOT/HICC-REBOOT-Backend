package hiccreboot.backend.common.dto.S3;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ImageRequest {

	private String fileName;
	private String fileNameExtension;
	private String key;
	private String url;

	@Builder(access = AccessLevel.PRIVATE)
	private ImageRequest(String fileName, String fileNameExtension, String key, String url) {
		this.fileName = fileName;
		this.fileNameExtension = fileNameExtension;
		this.key = key;
		this.url = url;
	}

	public static ImageRequest create(String fileName, String fileNameExtension, String key, String url) {
		return ImageRequest.builder()
			.fileName(fileName)
			.fileNameExtension(fileNameExtension)
			.key(key)
			.url(url)
			.build();
	}

}
