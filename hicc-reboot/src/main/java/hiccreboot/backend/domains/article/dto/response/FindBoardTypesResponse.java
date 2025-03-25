package hiccreboot.backend.domains.article.dto.response;

import hiccreboot.backend.domains.article.domain.BoardTypeEntity;

public record FindBoardTypesResponse(
	Long boardTypeId,
	String boardType
) {

	public static FindBoardTypesResponse from(BoardTypeEntity boardType) {
		return new FindBoardTypesResponse(boardType.getId(), boardType.getName());
	}
}
