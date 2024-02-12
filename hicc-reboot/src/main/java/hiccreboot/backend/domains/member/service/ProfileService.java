package hiccreboot.backend.domains.member.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.common.exception.DepartmentNotFoundException;
import hiccreboot.backend.common.exception.MemberNotFoundException;
import hiccreboot.backend.domains.article.domain.Article;
import hiccreboot.backend.domains.article.repository.ArticleRepository;
import hiccreboot.backend.domains.comment.domain.Comment;
import hiccreboot.backend.domains.comment.repository.CommentRepository;
import hiccreboot.backend.domains.department.domain.Department;
import hiccreboot.backend.domains.department.repository.DepartmentRepository;
import hiccreboot.backend.domains.member.domain.Member;
import hiccreboot.backend.domains.member.dto.request.ProfileModifyRequest;
import hiccreboot.backend.domains.member.dto.response.PersonalArticleResponse;
import hiccreboot.backend.domains.member.dto.response.PersonalCommentResponse;
import hiccreboot.backend.domains.member.dto.response.ProfileMemberResponse;
import hiccreboot.backend.domains.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfileService {

	private final MemberRepository memberRepository;
	private final ArticleRepository articleRepository;
	private final CommentRepository commentRepository;
	private final DepartmentRepository departmentRepository;
	private final PasswordEncoder passwordEncoder;

	private static final String PHONE_NUMBER_REGEX = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$";

	@Transactional(readOnly = true)
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
		modifyEmail(member, request.getEmail());
	}

	public void withdraw(String studentNumber) {
		memberRepository.findByStudentNumber(studentNumber)
			.ifPresentOrElse(this::deleteMember, () -> {
				throw MemberNotFoundException.EXCEPTION;
			});
	}

	private void modifyPhoneNumber(Member member, String phoneNumber) {
		if (phoneNumber == null || !phoneNumber.matches(PHONE_NUMBER_REGEX)) {
			return;
		}

		member.updatePhoneNumber(phoneNumber);
	}

	private void modifyDepartment(Member member, String departmentName) {
		if (departmentName == null) {
			return;
		}

		if (member.getDepartment().getName().equals(departmentName)) {
			return;
		}
		Department department = departmentRepository.findByName(departmentName)
			.orElseThrow(() -> DepartmentNotFoundException.EXCEPTION);

		member.updateDepartment(department);
	}

	private void modifyPassword(Member member, String password) {
		if (password == null || password.isBlank()) {
			return;
		}
		member.updatePassword(password);
		member.passwordEncode(passwordEncoder);
	}

	private void modifyEmail(Member member, String email) {
		if (email == null) {
			return;
		}
		member.updateEmail(email);
	}

	public DataResponse<Page<PersonalArticleResponse>> findPersonalArticles(int page, int size, String studentNumber) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

		Member member = memberRepository.findByStudentNumber(studentNumber)
			.orElseThrow(() -> MemberNotFoundException.EXCEPTION);

		Page<PersonalArticleResponse> result = articleRepository.findAllByMember(member, pageable)
			.map(PersonalArticleResponse::create);

		return DataResponse.ok(result);
	}

	public DataResponse<Page<PersonalCommentResponse>> findPersonalComments(int page, int size, String studentNumber) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

		Member member = memberRepository.findByStudentNumber(studentNumber)
			.orElseThrow(() -> MemberNotFoundException.EXCEPTION);

		Page<PersonalCommentResponse> result = commentRepository.findAllByMember(member, pageable)
			.map(PersonalCommentResponse::create);

		return DataResponse.ok(result);
	}

	private void deleteMember(Member deletedMember) {
		articleRepository.findAllByMember(deletedMember)
			.forEach(Article::deleteArticleSoftly);

		commentRepository.findAllByMember(deletedMember)
			.forEach(Comment::deleteCommentSoftly);

		memberRepository.delete(deletedMember);
	}
}
