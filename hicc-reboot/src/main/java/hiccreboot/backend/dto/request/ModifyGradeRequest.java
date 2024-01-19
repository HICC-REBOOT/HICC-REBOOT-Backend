package hiccreboot.backend.dto.request;

import hiccreboot.backend.domain.Grade;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ModifyGradeRequest {
	@NotNull
	private Grade grade;
}
