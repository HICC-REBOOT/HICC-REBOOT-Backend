package hiccreboot.backend.domains.image.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.domains.image.dto.request.SimpleImageRequest;
import hiccreboot.backend.domains.image.dto.response.ImageResponse;
import hiccreboot.backend.domains.image.service.S3Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/s3")
@RequiredArgsConstructor
public class S3Controller {

	private final S3Service s3Service;

	@PostMapping
	public DataResponse<ImageResponse> postPreSignedURLs(@Valid @RequestBody SimpleImageRequest simpleImageRequest) {
		return s3Service.makePreSignedUrls(simpleImageRequest);
	}
}
