package hiccreboot.backend.controller;

import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hiccreboot.backend.common.auth.jwt.TokenProvider;
import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.domain.Member;
import hiccreboot.backend.service.MainService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/main")
@RequiredArgsConstructor
public class MainController {

	private final MainService mainService;
	private final MemberService memberService;
	private final TokenProvider tokenProvider;

	@GetMapping("/member-count")
	public Object searchMemberCount() {
		Long memberCount = mainService.findMemberCount();

		return DataResponse.ok(memberCount);
	}

	@GetMapping("/latest-news")
	public Object searchLatestNews(
		@RequestParam(value = "page") int pageNumber,
		@RequestParam(value = "size") int pageSize,
		HttpServletRequest httpServletRequest) {
		Optional<String> studentNumber = tokenProvider.extractStudentNumber(httpServletRequest);
		Member member = memberService.findMemberByStudentNumber(studentNumber.get());

		return mainService.makeLatestNews(pageNumber, pageSize, member.getGrade(), member.getName());
	}
}


