package hiccreboot.backend.service;

import static hiccreboot.backend.common.consts.HttpConstants.*;

import java.util.Optional;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import hiccreboot.backend.common.dto.Register.IdDuplicationCheckResponse;
import hiccreboot.backend.common.exception.StudentNumberDuplicationException;
import hiccreboot.backend.domain.Member;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegisterService {
	
	private final MemberService memberService;

	public IdDuplicationCheckResponse isDuplicatedStudentNumber(String studentNumber) {
		Optional<Member> member = memberService.findByStudentNumber(studentNumber);

		if (member.isPresent()) {

			// 이 부분 리턴에 맞게 수정
			return IdDuplicationCheckResponse.createIdDuplicationResponse(true, HttpStatusCode.valueOf(OK));
		}
		throw StudentNumberDuplicationException.EXCEPTION;
	}
}
