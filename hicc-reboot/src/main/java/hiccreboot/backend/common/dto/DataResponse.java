package hiccreboot.backend.common.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataResponse<T> extends BaseResponse {
	private final T data;
	
	@Builder
	protected DataResponse(boolean isSuccess, HttpStatusCode statusCode, T data) {
		super(isSuccess, statusCode);
		this.data = data;
	}

	public static <T> DataResponse<T> ok(T data) {
		return DataResponse.<T>builder()
			.isSuccess(true)
			.statusCode(HttpStatus.OK)
			.data(data)
			.build();
	}

	public static <T> DataResponse<T> ok() {
		return DataResponse.<T>builder()
			.isSuccess(true)
			.statusCode(HttpStatus.OK)
			.build();
	}
}
