package hiccreboot.backend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.common.dto.S3.ImageResponse;
import hiccreboot.backend.common.dto.S3.SimpleImageRequest;
import hiccreboot.backend.service.S3Service;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/s3")
@RequiredArgsConstructor
public class S3Controller {

	private final S3Service s3Service;

	@PostMapping
	public DataResponse<ImageResponse> postPreSignedURLs(@RequestBody SimpleImageRequest simpleImageRequest) {
		return s3Service.makePreSignedUrls(simpleImageRequest);
	}
}
