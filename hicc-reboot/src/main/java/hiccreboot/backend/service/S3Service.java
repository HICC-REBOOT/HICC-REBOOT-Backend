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
import hiccreboot.backend.common.dto.S3.ImageRequest;
import hiccreboot.backend.common.dto.S3.ImageResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class S3Service {
	private final AmazonS3 amazonS3;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	private final int TIME_LIMIT = 1000 * 60 * 2;

	public BaseResponse getPreSignedUrls(List<ImageRequest> imageRequests) {

		List<ImageResponse> response = new ArrayList<>();
		for (ImageRequest imageRequest : imageRequests) {
			String key = UUID.randomUUID() + imageRequest.getFileName();
			String fileNameExtention = imageRequest.getFileNameExtention();

			//s3 디렉터리 경로 설정
			if (!fileNameExtention.equals("")) {
				key = fileNameExtention + "/" + key;
			}

			response.add(ImageResponse.create(imageRequest.getFileName(), key, getPreSignedUrl(key)));
		}

		return DataResponse.ok(response);
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