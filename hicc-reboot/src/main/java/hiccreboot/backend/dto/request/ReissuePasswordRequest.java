package hiccreboot.backend.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Getter;

@Getter
public class ReissuePasswordRequest {
	@Email
	private String email;
}
