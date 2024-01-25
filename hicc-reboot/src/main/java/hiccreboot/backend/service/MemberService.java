package hiccreboot.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hiccreboot.backend.common.dto.BaseResponse;
import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.common.exception.DepartmentNotFoundException;
import hiccreboot.backend.common.exception.MemberNotFoundException;
import hiccreboot.backend.common.exception.StudentDuplicateException;
import hiccreboot.backend.domain.Department;
import hiccreboot.backend.domain.Grade;
import hiccreboot.backend.domain.Member;
import hiccreboot.backend.dto.request.ProfileModifyRequest;
import hiccreboot.backend.dto.request.SignUpRequest;
import hiccreboot.backend.dto.request.StudentNumberCheckRequest;
import hiccreboot.backend.dto.response.ApplicantResponse;
import hiccreboot.backend.dto.response.MemberResponse;
import hiccreboot.backend.dto.response.MemberSimpleResponse;
import hiccreboot.backend.dto.response.PersonalArticleResponse;
import hiccreboot.backend.dto.response.PersonalCommentResponse;
import hiccreboot.backend.dto.response.ProfileMemberResponse;
import hiccreboot.backend.repository.Article.ArticleRepository;
import hiccreboot.backend.repository.Comment.CommentRepository;
import hiccreboot.backend.repository.department.DepartmentRepository;
import hiccreboot.backend.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;
	private final DepartmentRepository departmentRepository;
	private final PasswordEncoder passwordEncoder;
	private final ArticleRepository articleRepository;
	private final CommentRepository commentRepository;

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

	@Transactional(readOnly = true)
	public DataResponse<Page<ApplicantResponse>> findAllApplicant(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<ApplicantResponse> result = memberRepository.findAllByGrade(Grade.APPLICANT, pageable)
			.map(ApplicantResponse::create);

		return DataResponse.ok(result);
	}

	public void approve(Long applicantId) {
		memberRepository.findById(applicantId)
			.filter(applicant -> applicant.getGrade().equals(Grade.APPLICANT))
			.ifPresentOrElse(Member::approve, () -> {
				throw MemberNotFoundException.EXCEPTION;
			});
	}

	public void reject(Long applicantId) {
		memberRepository.findById(applicantId)
			.filter(applicant -> applicant.getGrade().equals(Grade.APPLICANT))
			.ifPresentOrElse(memberRepository::delete, () -> {
				throw MemberNotFoundException.EXCEPTION;
			});
	}

	@Transactional(readOnly = true)
	public DataResponse<Page<MemberResponse>> findMembers(int page, int size, String sortBy, String search) {
		Pageable pageable = PageRequest.of(page, size, getSort(sortBy));

		Page<MemberResponse> result = memberRepository.findAllByNameWithoutApplicant(search, pageable)
			.map(MemberResponse::create);

		return DataResponse.ok(result);
	}

	public void modifyGrade(Long modifiedMemberId, Grade grade, String presidentStudentNumber) {
		memberRepository.findById(modifiedMemberId)
			.filter(modifiedMember -> validateTargetNotRequester(modifiedMember, presidentStudentNumber))
			.filter(modifiedMember -> validateModifyAnotherGrade(modifiedMember, grade))
			.ifPresentOrElse(member -> member.updateGrade(grade), () -> {
				throw MemberNotFoundException.EXCEPTION;
			});
	}

	public void expel(Long deletedMemberId, String presidentStudentNumber) {
		memberRepository.findById(deletedMemberId)
			.filter(deletedMember -> validateTargetNotRequester(deletedMember, presidentStudentNumber))
			.ifPresentOrElse(memberRepository::delete, () -> {
				throw MemberNotFoundException.EXCEPTION;
			});
	}

	private Sort getSort(String sortBy) {
		if (sortBy.equals("department")) {
			return Sort.by("department_id");
		} else if (sortBy.equals("name")) {
			return Sort.by("name");
		} else {
			return Sort.by("grade");
		}
	}

	private boolean validateTargetNotRequester(Member member, String requesterStudentNumber) {
		return !member.getStudentNumber().equals(requesterStudentNumber);
	}

	private boolean validateModifyAnotherGrade(Member modifiedMember, Grade modifiedGrade) {
		return !modifiedMember.getGrade().equals(modifiedGrade);
	}

	public DataResponse<ProfileMemberResponse> getProfile(String studentNumber) {
		ProfileMemberResponse response = memberRepository.findByStudentNumber(studentNumber)
			.map(ProfileMemberResponse::create)
			.orElseThrow(() -> MemberNotFoundException.EXCEPTION);

		return DataResponse.ok(response);
	}

	public void modifyProfile(ProfileModifyRequest request, String studentNumber) {
		Member member = memberRepository.findByStudentNumber(studentNumber)
			.orElseThrow(() -> MemberNotFoundException.EXCEPTION);

		modifyPhoneNumber(member, request.getPhoneNumber());
		modifyDepartment(member, request.getDepartment());
		modifyPassword(member, request.getPassword());
	}

	public void withdraw(String studentNumber) {
		memberRepository.findByStudentNumber(studentNumber)
			.ifPresentOrElse(memberRepository::delete, () -> {
				throw MemberNotFoundException.EXCEPTION;
			});
	}

	private void modifyPhoneNumber(Member member, String phoneNumber) {
		member.updatePhoneNumber(phoneNumber);
	}

	private void modifyDepartment(Member member, String departmentName) {
		if (member.getDepartment().getName().equals(departmentName)) {
			return;
		}
		Department department = departmentRepository.findByName(departmentName)
			.orElseThrow(() -> DepartmentNotFoundException.EXCEPTION);

		member.updateDepartment(department);
	}

	private void modifyPassword(Member member, String password) {

		member.updatePassword(password);
		member.passwordEncode(passwordEncoder);
	}

	public DataResponse<Page<PersonalArticleResponse>> findPersonalArticles(int page, int size, String studentNumber) {
		Pageable pageable = PageRequest.of(page, size);

		Member member = memberRepository.findByStudentNumber(studentNumber)
			.orElseThrow(() -> MemberNotFoundException.EXCEPTION);

		Page<PersonalArticleResponse> result = articleRepository.findAllByMember(member, pageable)
			.map(PersonalArticleResponse::create);

		return DataResponse.ok(result);
	}

	public DataResponse<Page<PersonalCommentResponse>> findPersonalComments(int page, int size, String studentNumber) {
		Pageable pageable = PageRequest.of(page, size);

		Member member = memberRepository.findByStudentNumber(studentNumber)
			.orElseThrow(() -> MemberNotFoundException.EXCEPTION);

		Page<PersonalCommentResponse> result = commentRepository.findAllByMember(member, pageable)
			.map(PersonalCommentResponse::create);

		return DataResponse.ok(result);
	}
}
