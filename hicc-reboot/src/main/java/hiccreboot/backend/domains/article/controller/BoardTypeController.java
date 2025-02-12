package hiccreboot.backend.domains.article.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hiccreboot.backend.common.dto.BaseResponse;
import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.domains.article.dto.request.CreateBoardTypeRequest;
import hiccreboot.backend.domains.article.dto.response.FindBoardTypesResponse;
import hiccreboot.backend.domains.article.service.BoardTypeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor
public class BoardTypeController {

	private final BoardTypeService boardTypeService;


	@GetMapping("/board-types")
	@Operation(summary = "게시판 목록 조회")
	public DataResponse<List<FindBoardTypesResponse>> findBoardTypes() {
		List<FindBoardTypesResponse> responses = boardTypeService.findBoardTypes();

		return DataResponse.ok(responses);
	}

	@PostMapping("/board-types")
	@Operation(summary = "게시판 목록 추가")
	public BaseResponse createBoardType(@RequestBody CreateBoardTypeRequest request) {
		boardTypeService.createBoardType(request.name());

		return DataResponse.noContent();
	}

	@DeleteMapping("/board-types/{board-type-id}")
	@Operation(summary = "게시판 목록 삭제")
	public BaseResponse deleteBoardType(@PathVariable("board-type-id") Long boardTypeId) {
		boardTypeService.deleteBoardType(boardTypeId);

		return DataResponse.noContent();
	}

	@PatchMapping("/board-types/{board-type-id}")
	@Operation(summary = "게시판 목록 수정")
	public BaseResponse updateBoardType(
		@PathVariable("board-type-id") Long boardTypeId,
		@RequestBody CreateBoardTypeRequest request
	) {
		boardTypeService.updateBoardType(boardTypeId, request.name());

		return DataResponse.noContent();
	}
}
