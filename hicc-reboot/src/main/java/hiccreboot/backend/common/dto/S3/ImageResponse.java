package hiccreboot.backend.common.dto.S3;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ImageResponse {

	private String fileName;
	private String key;
	private String preSignedUrl;

	@Builder(access = AccessLevel.PRIVATE)
	private ImageResponse(String fileName, String key, String preSignedUrl) {
		this.fileName = fileName;
		this.key = key;
		this.preSignedUrl = preSignedUrl;
	}

	public static ImageResponse create(String fileName, String key, String preSignedUrl) {
		return ImageResponse.builder()
			.fileName(fileName)
			.key(key)
			.preSignedUrl(preSignedUrl)
			.build();
	}
}
