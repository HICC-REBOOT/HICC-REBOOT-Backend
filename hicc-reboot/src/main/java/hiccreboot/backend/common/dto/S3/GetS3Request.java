package hiccreboot.backend.common.dto.S3;

import java.util.List;

import lombok.Getter;

@Getter
public class GetS3Request {

	private List<SimpleImageRequest> images;
}
