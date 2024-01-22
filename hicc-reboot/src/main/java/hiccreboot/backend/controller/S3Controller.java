package hiccreboot.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hiccreboot.backend.common.dto.BaseResponse;
import hiccreboot.backend.common.dto.S3.GetS3Request;
import hiccreboot.backend.service.S3Service;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/s3")
@RequiredArgsConstructor
public class S3Controller {

	private final S3Service s3Service;

	@GetMapping
	public BaseResponse getPreSignedURLs(@RequestBody GetS3Request getS3Request) {
		return s3Service.getPreSignedUrls(getS3Request.getImages());
	}
}
