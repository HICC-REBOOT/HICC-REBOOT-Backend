package hiccreboot.backend.domains.auth.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Getter;

@Getter
public class CreateNonceRequest {
	@Email
	private String email;
}
