package hiccreboot.backend.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import hiccreboot.backend.common.dto.BaseResponse;
import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.common.dto.S3.ImageResponse;
import hiccreboot.backend.common.dto.S3.SimpleImageRequest;
import hiccreboot.backend.common.exception.FileNameExtenstionNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class S3Service {

	private final String FILE_NAME_EXTENSION_JPG = "JPG";
	private final String FILE_NAME_EXTENSION_JPEG = "JPEG";
	private final String FILE_NAME_EXTENSION_PNG = "PNG";

	private final AmazonS3 amazonS3;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	private final int TIME_LIMIT = 1000 * 60 * 2;

	public BaseResponse makePreSignedUrls(List<SimpleImageRequest> simpleImageRequests) {
		//확장자 검사
		simpleImageRequests.stream()
			.forEach(simpleImageRequest -> checkFileNameExtension(simpleImageRequest.getFileNameExtension()));

		List<ImageResponse> response = new ArrayList<>();
		for (SimpleImageRequest simpleImageRequest : simpleImageRequests) {
			String key = UUID.randomUUID() + simpleImageRequest.getFileName();
			String fileNameExtension = simpleImageRequest.getFileNameExtension().toUpperCase();

			//s3 디렉터리 경로 설정
			if (!fileNameExtension.equals("")) {
				key = fileNameExtension + "/" + key;
			}

			response.add(ImageResponse.create(simpleImageRequest.getFileName(), key, getPreSignedUrl(key)));
		}

		return DataResponse.ok(response);
	}

	private void checkFileNameExtension(String fileNameExtension) {
		fileNameExtension = fileNameExtension.toUpperCase();

		System.out.println(fileNameExtension);

		if (fileNameExtension.equals(FILE_NAME_EXTENSION_JPG)
			|| fileNameExtension.equals(FILE_NAME_EXTENSION_JPEG)
			|| fileNameExtension.equals(FILE_NAME_EXTENSION_PNG)) {
			return;
		}

		throw FileNameExtenstionNotFoundException.EXCEPTION;
	}

	private String getPreSignedUrl(String fileName) {
		GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePreSignedUrlRequest(fileName);
		URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
		return url.toString();
	}

	private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(String fileName) {
		GeneratePresignedUrlRequest generatePresignedUrlRequest =
			new GeneratePresignedUrlRequest(bucket, fileName)
				.withMethod(HttpMethod.PUT)
				.withExpiration(getPreSignedUrlExpiration());
		generatePresignedUrlRequest.addRequestParameter(
			Headers.S3_CANNED_ACL,
			CannedAccessControlList.PublicRead.toString());
		return generatePresignedUrlRequest;
	}

	private Date getPreSignedUrlExpiration() {
		Date expiration = new Date();
		long expTimeMillis = expiration.getTime() + TIME_LIMIT;
		expiration.setTime(expTimeMillis);
		return expiration;
	}

	public void deleteImage(String fileName) {
		amazonS3.deleteObject(bucket, fileName);
	}
}