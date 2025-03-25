package hiccreboot.backend.domains.image.dto.response;

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
	private String url;

	@Builder(access = AccessLevel.PRIVATE)
	private ImageResponse(String fileName, String key, String preSignedUrl, String url) {
		this.fileName = fileName;
		this.key = key;
		this.preSignedUrl = preSignedUrl;
		this.url = url;
	}

	public static ImageResponse create(String fileName, String key, String preSignedUrl, String url) {
		return ImageResponse.builder()
			.fileName(fileName)
			.key(key)
			.preSignedUrl(preSignedUrl)
			.url(url)
			.build();
	}
}
