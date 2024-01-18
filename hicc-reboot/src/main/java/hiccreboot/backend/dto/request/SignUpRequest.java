package hiccreboot.backend.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import hiccreboot.backend.domain.Department;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SignUpRequest {
	@NotBlank
	@Pattern(regexp = "[A-Z]\\d{6}", message = "대문자 학번으로 입력해주세요.")
	private String studentNumber;

	@NotBlank
	@Pattern(regexp = "[a-zA-Z가-힣]{1,6}", message = "이름을 확인해주세요.")
	private String name;

	@NotNull
	private String password;

	@NotBlank
	@Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", message = "전화번호를 확인해주세요.")
	private String phoneNumber;

	private Department department;
}
