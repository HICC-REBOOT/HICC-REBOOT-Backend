package hiccreboot.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hiccreboot.backend.common.dto.BaseResponse;
import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.common.exception.MemberNotFoundException;
import hiccreboot.backend.common.exception.StudentDuplicateException;
import hiccreboot.backend.domain.Department;
import hiccreboot.backend.domain.Grade;
import hiccreboot.backend.domain.Member;
import hiccreboot.backend.dto.request.SignUpRequest;
import hiccreboot.backend.dto.request.StudentNumberCheckRequest;
import hiccreboot.backend.dto.response.ApplicantResponse;
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

	public DataResponse<Page<ApplicantResponse>> findAllApplicant(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Member> applicants = memberRepository.findAllByGrade(Grade.APPLICANT, pageable);

		return DataResponse.ok(applicants.map(ApplicantResponse::create));
	}

	public BaseResponse approve(Long applicantId) {
		memberRepository.findById(applicantId)
			.filter(applicant -> applicant.getGrade().equals(Grade.APPLICANT))
			.ifPresentOrElse(applicant -> applicant.updateGrade(Grade.NORMAL), () -> {
				throw MemberNotFoundException.EXCEPTION;
			});

		return DataResponse.ok();
	}

	public BaseResponse reject(Long applicantId) {
		memberRepository.findById(applicantId)
			.filter(applicant -> applicant.getGrade().equals(Grade.APPLICANT))
			.ifPresentOrElse(memberRepository::delete, () -> {
				throw MemberNotFoundException.EXCEPTION;
			});

		return DataResponse.noContent();
	}

}
