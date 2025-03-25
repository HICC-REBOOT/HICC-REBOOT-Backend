package hiccreboot.backend.domains.auth.dto.request;

public record LoginRequest(
	String studentNumber,
	String password
) {
}
