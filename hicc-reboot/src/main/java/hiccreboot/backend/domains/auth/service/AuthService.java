package hiccreboot.backend.domains.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hiccreboot.backend.common.dto.BaseResponse;
import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.common.exception.DepartmentNotFoundException;
import hiccreboot.backend.common.exception.PasswordResetNotFoundException;
import hiccreboot.backend.common.exception.StudentDuplicateException;
import hiccreboot.backend.domains.auth.domain.PasswordReset;
import hiccreboot.backend.domains.auth.dto.request.SignUpRequest;
import hiccreboot.backend.domains.auth.dto.request.StudentNumberCheckRequest;
import hiccreboot.backend.domains.auth.repository.PasswordResetRepository;
import hiccreboot.backend.domains.department.domain.Department;
import hiccreboot.backend.domains.department.repository.DepartmentRepository;
import hiccreboot.backend.domains.member.domain.Member;
import hiccreboot.backend.domains.member.dto.response.MemberSimpleResponse;
import hiccreboot.backend.domains.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

	private final MemberRepository memberRepository;
	private final PasswordResetRepository passwordResetRepository;
	private final PasswordEncoder passwordEncoder;
	private final DepartmentRepository departmentRepository;

	public void signUp(SignUpRequest request) {

		if (memberRepository.findByStudentNumber(request.getStudentNumber()).isPresent()) {
			throw StudentDuplicateException.EXCEPTION;
		}
		String studentNumber = request.getStudentNumber();
		String name = request.getName();
		String password = request.getPassword();
		String phoneNumber = request.getPhoneNumber();
		Department department = departmentRepository.findByName(request.getDepartment())
			.orElseThrow(() -> DepartmentNotFoundException.EXCEPTION);
		String email = request.getEmail();

		Member member = Member.signUp(studentNumber, department, name, password, phoneNumber, email);

		member.passwordEncode(passwordEncoder);
		memberRepository.save(member);
	}

	@Transactional(readOnly = true)
	public BaseResponse checkDuplicate(StudentNumberCheckRequest request) {
		memberRepository.findByStudentNumber(request.getStudentNumber())
			.ifPresent(member -> {
				throw StudentDuplicateException.EXCEPTION;
			});
		return DataResponse.ok();
	}

	@Transactional(readOnly = true)
	public BaseResponse findSimpleInfo(String studentNumber) {
		return memberRepository.findByStudentNumber(studentNumber)
			.map(MemberSimpleResponse::create)
			.map(DataResponse::ok)
			.orElseThrow();
	}

	public void resetPassword(String nonce, String password) {
		Long memberId = passwordResetRepository.findByNonce(nonce)
			.map(PasswordReset::getId)
			.orElseThrow(() -> PasswordResetNotFoundException.EXCEPTION);

		memberRepository.findById(memberId)
			.ifPresent(member -> {
				passwordResetRepository.deleteById(member.getId());

				member.updatePassword(password);
				member.passwordEncode(passwordEncoder);
			});
	}

}
