package hiccreboot.backend.common.dto.S3;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SimpleImageRequest {

	private String fileName;
	private String fileNameExtension;

	@Builder(access = AccessLevel.PRIVATE)
	private SimpleImageRequest(String fileName, String fileNameExtension) {
		this.fileName = fileName;
		this.fileNameExtension = fileNameExtension;
	}

	public static SimpleImageRequest create(String fileName, String fileNameExtension) {
		return SimpleImageRequest.builder()
			.fileName(fileName)
			.fileNameExtension(fileNameExtension)
			.build();
	}
}
