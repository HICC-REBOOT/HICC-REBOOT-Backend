package hiccreboot.backend.domains.member.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.common.exception.MemberNotFoundException;
import hiccreboot.backend.domains.article.domain.Article;
import hiccreboot.backend.domains.article.repository.ArticleRepository;
import hiccreboot.backend.domains.comment.domain.Comment;
import hiccreboot.backend.domains.comment.repository.CommentRepository;
import hiccreboot.backend.domains.member.domain.Grade;
import hiccreboot.backend.domains.member.domain.Member;
import hiccreboot.backend.domains.member.dto.response.ApplicantResponse;
import hiccreboot.backend.domains.member.dto.response.MemberResponse;
import hiccreboot.backend.domains.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

	private final MemberRepository memberRepository;
	private final ArticleRepository articleRepository;
	private final CommentRepository commentRepository;

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
			.ifPresentOrElse(this::deleteMember, () -> {
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
		if (grade.equals(Grade.PRESIDENT)) {
			handOverPresident(modifiedMemberId, presidentStudentNumber);
			return;
		}

		memberRepository.findById(modifiedMemberId)
			.filter(modifiedMember -> validateTargetNotRequester(modifiedMember, presidentStudentNumber))
			.ifPresent(modifiedMember -> modifiedMember.updateGrade(grade));
	}

	private void handOverPresident(Long modifiedMemberId, String presidentStudentNumber) {
		Member nextPresident = memberRepository.findById(modifiedMemberId)
			.filter(modifiedMember -> validateTargetNotRequester(modifiedMember, presidentStudentNumber))
			.orElseThrow(() -> MemberNotFoundException.EXCEPTION);

		memberRepository.findByStudentNumber(presidentStudentNumber)
			.ifPresent(existingPresident -> {
				existingPresident.updateGrade(Grade.EXECUTIVE);
				nextPresident.updateGrade(Grade.PRESIDENT);
			});
	}

	public void expel(Long deletedMemberId, String presidentStudentNumber) {
		memberRepository.findById(deletedMemberId)
			.filter(deletedMember -> validateTargetNotRequester(deletedMember, presidentStudentNumber))
			.ifPresentOrElse(this::deleteMember, () -> {
				throw MemberNotFoundException.EXCEPTION;
			});
	}

	private void deleteMember(Member deletedMember) {
		articleRepository.findAllByMember(deletedMember)
			.forEach(Article::deleteArticleSoftly);

		commentRepository.findAllByMember(deletedMember)
			.forEach(Comment::deleteCommentSoftly);

		memberRepository.delete(deletedMember);
	}

	private Sort getSort(String sortBy) {
		String uppercaseSortBy = sortBy.toUpperCase();
		if (uppercaseSortBy.equals("DEPARTMENT")) {
			return Sort.by("department.name");
		} else if (uppercaseSortBy.equals("NAME")) {
			return Sort.by("name");
		} else {
			return Sort.by("grade");
		}
	}

	private boolean validateTargetNotRequester(Member member, String requesterStudentNumber) {
		return !member.getStudentNumber().equals(requesterStudentNumber);
	}
}
