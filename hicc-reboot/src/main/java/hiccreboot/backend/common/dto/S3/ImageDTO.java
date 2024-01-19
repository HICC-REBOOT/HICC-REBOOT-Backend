package hiccreboot.backend.common.dto.S3;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ImageDTO {

	private String fileName;
	private String fileNameExtention;
	private String key;
	private String url;

	@Builder(access = AccessLevel.PRIVATE)
	private ImageDTO(String fileName, String fileNameExtention, String key, String url) {
		this.fileName = fileName;
		this.key = key;
		this.url = url;
		this.fileNameExtention = fileNameExtention;
	}

	public static ImageDTO create(String fileName, String fileNameExtention) {
		return ImageDTO.builder()
			.fileName(fileName)
			.fileNameExtention(fileNameExtention)
			.build();
	}

	public static ImageDTO create(String key, String fileNameExtention, String preSignedUrl) {
		return ImageDTO.builder()
			.key(key)
			.fileNameExtention(fileNameExtention)
			.url(preSignedUrl)
			.build();
	}
}
