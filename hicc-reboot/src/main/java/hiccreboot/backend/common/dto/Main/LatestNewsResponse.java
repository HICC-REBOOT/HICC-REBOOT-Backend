package hiccreboot.backend.common.dto.Main;

import java.time.LocalDateTime;

import hiccreboot.backend.domain.Grade;

public record LatestNewsResponse(Long articleId, Grade grade, String name, LocalDateTime date,
								 Boolean imageExistence, String subject) {
}
