package hiccreboot.backend.common.dto.S3;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ImageRequest {

	private String fileName;
	private String fileNameExtention;
	private String key;
	private String url;

	@Builder(access = AccessLevel.PRIVATE)
	private ImageRequest(String fileName, String fileNameExtention, String key, String url) {
		this.fileName = fileName;
		this.fileNameExtention = fileNameExtention;
		this.key = key;
		this.url = url;
	}

	public static ImageRequest create(String fileName, String fileNameExtention, String key, String url) {
		return ImageRequest.builder()
			.fileName(fileName)
			.fileNameExtention(fileNameExtention)
			.key(key)
			.url(url)
			.build();
	}

}
