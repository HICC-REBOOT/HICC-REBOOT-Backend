package hiccreboot.backend.common.dto;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatusCode;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DataListResponse<T> extends BaseResponse {

	private final List<T> data;
	private final int count;

	@Builder
	protected DataListResponse(boolean isSuccess, HttpStatusCode statusCode, List<T> data) {
		super(isSuccess, statusCode);
		this.data = data;
		this.count = data.size();
	}

	public static <T> DataListResponse<T> create(List<T> data) {
		return DataListResponse.<T>builder()
			.data(data)
			.build();
	}

	public static <T> DataListResponse<T> create(T... data) {
		return DataListResponse.<T>builder()
			.data(Arrays.asList(data))
			.build();
	}
}
