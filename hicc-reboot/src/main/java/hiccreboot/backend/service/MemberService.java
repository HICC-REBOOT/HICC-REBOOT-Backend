package hiccreboot.backend.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hiccreboot.backend.common.dto.BaseResponse;
import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.common.exception.StudentDuplicateException;
import hiccreboot.backend.domain.Department;
import hiccreboot.backend.domain.Member;
import hiccreboot.backend.dto.request.SignUpRequest;
import hiccreboot.backend.dto.request.StudentNumberCheckRequest;
import hiccreboot.backend.dto.response.MemberSimpleResponse;
import hiccreboot.backend.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	public void signUp(SignUpRequest request) {

		if (memberRepository.findByStudentNumber(request.getStudentNumber()).isPresent()) {
			throw StudentDuplicateException.EXCEPTION;
		}
		String studentNumber = request.getStudentNumber();
		String name = request.getName();
		String password = request.getPassword();
		String phoneNumber = request.getPhoneNumber();
		Department department = request.getDepartment();

		Member member = Member.signUp(studentNumber, department, name, password, phoneNumber);

		member.passwordEncode(passwordEncoder);
		memberRepository.save(member);
	}

	public BaseResponse checkDuplicate(StudentNumberCheckRequest request) {
		memberRepository.findByStudentNumber(request.getStudentNumber())
			.ifPresent(member -> {
				throw StudentDuplicateException.EXCEPTION;
			});
		return DataResponse.ok();
	}

	public BaseResponse findSimpleInfo(String studentNumber) {
		return memberRepository.findByStudentNumber(studentNumber)
			.map(MemberSimpleResponse::create)
			.map(DataResponse::ok)
			.orElseThrow();
	}

	public Member findByStudentNumber(String studentNumber) {

	}
}
