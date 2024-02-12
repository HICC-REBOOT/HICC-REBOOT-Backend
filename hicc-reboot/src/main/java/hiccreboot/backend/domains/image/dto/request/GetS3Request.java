package hiccreboot.backend.domains.image.dto.request;

import java.util.List;

import lombok.Getter;

@Getter
public class GetS3Request {

	private List<SimpleImageRequest> images;
}
