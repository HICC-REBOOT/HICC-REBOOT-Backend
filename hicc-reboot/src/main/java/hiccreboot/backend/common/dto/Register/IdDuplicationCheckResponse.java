package hiccreboot.backend.common.dto.Register;

import org.springframework.http.HttpStatusCode;

import hiccreboot.backend.common.dto.BaseResponse;

public class IdDuplicationCheckResponse extends BaseResponse {
	protected IdDuplicationCheckResponse(boolean isSuccess, HttpStatusCode statusCode) {
		super(isSuccess, statusCode);
	}

	public static IdDuplicationCheckResponse createIdDuplicationResponse(boolean isSuccess,
		HttpStatusCode statusCode) {
		return new IdDuplicationCheckResponse(isSuccess, statusCode);
	}
}
