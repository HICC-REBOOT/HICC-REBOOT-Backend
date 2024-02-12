package hiccreboot.backend.domains.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudentNumberCheckRequest {

	@NotBlank
	@Pattern(regexp = "[A-Z]\\d{6}", message = "대문자 학번으로 입력해주세요.")
	private String studentNumber;
}
