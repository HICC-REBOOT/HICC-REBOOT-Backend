package hiccreboot.backend.domains.main.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.domains.main.dto.response.FooterResponse;
import hiccreboot.backend.domains.main.service.MainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@Tag(name = "메인 페이지 API", description = "회원수, 최근 뉴스")
@RequestMapping("/api/main")
@RequiredArgsConstructor
public class MainController {

	private final MainService mainService;

	@GetMapping("/member-count")
	@Operation(summary = "총 회원 수", description = "총 회원 수를 반환하는 api")
	@ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = DataResponse.class))})
	public DataResponse<Long> searchMemberCount() {
		Long memberCount = mainService.findMemberCount();

		return DataResponse.ok(memberCount);
	}

	@GetMapping("/latest-news")
	@Operation(summary = "최근 뉴스", description = "최근 게시글 3개를 반환하며, 요청에 주의")
	@ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = DataResponse.class))})
	public DataResponse searchLatestNews(
		@RequestParam(value = "page") int pageNumber,
		@RequestParam(value = "size") int pageSize) {
		return mainService.makeLatestNews(pageNumber, pageSize);
	}

	@GetMapping("/footer")
	public DataResponse<FooterResponse> footer() {
		return mainService.footerResponse();
	}
}


