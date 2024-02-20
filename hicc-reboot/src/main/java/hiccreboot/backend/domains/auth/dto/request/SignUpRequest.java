package hiccreboot.backend.domains.auth.dto.request;

import hiccreboot.backend.domains.department.domain.Department;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(title = "회원가입 요청 DTO")
public class SignUpRequest {
	@NotNull
	@Pattern(regexp = "[A-Z]\\d{6}", message = "대문자 학번으로 입력해주세요.")
	private String studentNumber;

	@NotNull
	@Pattern(regexp = "[가-힣]{2,7}", message = "이름을 확인해주세요.")
	private String name;

	@NotNull
	private String password;

	@NotNull
	@Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", message = "전화번호를 확인해주세요.")
	private String phoneNumber;

	@NotNull
	@Schema(example = "컴퓨터공학과", implementation = Department.class)
	private String department;

	@NotNull
	@Email
	private String email;
}
