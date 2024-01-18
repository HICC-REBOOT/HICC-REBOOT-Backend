package hiccreboot.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.service.MainService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/main")
@RequiredArgsConstructor
public class MainController {

	private final MainService mainService;

	@GetMapping("/member-count")
	public DataResponse<Long> searchMemberCount() {
		Long memberCount = mainService.findMemberCount();

		return DataResponse.ok(memberCount);
	}

	@GetMapping("/latest-news")
	public DataResponse searchLatestNews(
		@RequestParam(value = "page") int pageNumber,
		@RequestParam(value = "size") int pageSize) {
		return mainService.makeLatestNews(pageNumber, pageSize);
	}
}


