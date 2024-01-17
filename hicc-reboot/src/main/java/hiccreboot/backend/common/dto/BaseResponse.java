package hiccreboot.backend.common.dto;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatusCode;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse {
	@JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
	private final LocalDateTime timestamp = LocalDateTime.now();
	private final boolean isSuccess;
	private final HttpStatusCode statusCode;

	protected BaseResponse(boolean isSuccess, HttpStatusCode statusCode) {
		this.isSuccess = isSuccess;
		this.statusCode = statusCode;
	}

}
