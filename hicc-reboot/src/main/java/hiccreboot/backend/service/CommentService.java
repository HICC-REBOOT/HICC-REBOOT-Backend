package hiccreboot.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hiccreboot.backend.common.dto.Comment.ChildCommentResponse;
import hiccreboot.backend.common.dto.Comment.ParentCommentResponse;
import hiccreboot.backend.common.dto.Comment.PostCommentRequest;
import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.common.exception.AccessForbiddenException;
import hiccreboot.backend.common.exception.ArticleNotFoundException;
import hiccreboot.backend.common.exception.CommentNotFoundException;
import hiccreboot.backend.common.exception.MemberNotFoundException;
import hiccreboot.backend.domain.Article;
import hiccreboot.backend.domain.Comment;
import hiccreboot.backend.domain.CommentGrade;
import hiccreboot.backend.domain.Grade;
import hiccreboot.backend.domain.Member;
import hiccreboot.backend.repository.Article.ArticleRepository;
import hiccreboot.backend.repository.Comment.CommentRepository;
import hiccreboot.backend.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
	private final Long PARENT_COMMENT = -1L;

	private final CommentRepository commentRepository;
	private final ArticleRepository articleRepository;
	private final MemberRepository memberRepository;

	public List<Comment> findParentComments(Long articleId) {
		return commentRepository.findAllByArticle_IdAndParentCommentIdLessThanEqual(articleId, PARENT_COMMENT);
	}

	public List<Comment> findChildComments(Long articleId) {
		return commentRepository.findAllByArticle_IdAndParentCommentIdGreaterThan(articleId, PARENT_COMMENT);
	}

	public DataResponse<List<ParentCommentResponse>> makeParentComments(Long articleId, String studentNumber) {
		Member member = memberRepository.findByStudentNumber(studentNumber)
			.orElseThrow(() -> MemberNotFoundException.EXCEPTION);

		List<ParentCommentResponse> parentCommentResponses = new ArrayList<>();
		findParentComments(articleId).stream()
			.forEach((parentComment) -> parentCommentResponses.add(new ParentCommentResponse(
				parentComment,
				parentComment.getMember() == member)));

		return DataResponse.ok(parentCommentResponses);
	}

	public DataResponse<List<ChildCommentResponse>> makeChildComments(Long articleId, String studentNumber) {
		Member member = memberRepository.findByStudentNumber(studentNumber)
			.orElseThrow(() -> MemberNotFoundException.EXCEPTION);

		List<ChildCommentResponse> childCommentResponses = new ArrayList<>();
		findChildComments(articleId).stream()
			.forEach((childComment) -> childCommentResponses.add(new ChildCommentResponse(
				childComment,
				childComment.getMember() == member)));

		return DataResponse.ok(childCommentResponses);
	}

	@Transactional
	public Comment saveComment(String studentNumber, PostCommentRequest postCommentRequest) {
		Article article = articleRepository.findById(postCommentRequest.getArticleId())
			.orElseThrow(() -> ArticleNotFoundException.EXCEPTION);
		Member member = memberRepository.findByStudentNumber(studentNumber)
			.orElseThrow(() -> MemberNotFoundException.EXCEPTION);

		checkSaveAuthority(member);

		Comment comment;
		if (postCommentRequest.getParentCommentId() > PARENT_COMMENT) {
			//부모 댓글이 있는 지 확인
			commentRepository.findById(postCommentRequest.getParentCommentId())
				.orElseThrow(() -> CommentNotFoundException.EXCEPTION);

			comment = Comment.createChildComment(
				postCommentRequest.getParentCommentId(),
				member,
				makeCommentGradeByMemberGrade(member.getGrade()),
				article,
				postCommentRequest.getContent());
		} else {
			comment = Comment.createParentComment(member, makeCommentGradeByMemberGrade(member.getGrade()), article,
				postCommentRequest.getContent());
		}

		return commentRepository.save(comment);
	}

	private void checkSaveAuthority(Member member) {
		if (member.getGrade() == Grade.APPLICANT) {
			throw AccessForbiddenException.EXCEPTION;
		}
	}

	private CommentGrade makeCommentGradeByMemberGrade(Grade grade) {
		if (grade == Grade.EXECUTIVE || grade == Grade.PRESIDENT) {
			return CommentGrade.EXECUTIVE;
		}
		if (grade == Grade.NORMAL) {
			return CommentGrade.NORMAL;
		}
		throw AccessForbiddenException.EXCEPTION;
	}

	@Transactional
	public void deleteComment(Long id, String studentNumber) {
		Member member = memberRepository.findByStudentNumber(studentNumber)
			.orElseThrow(() -> MemberNotFoundException.EXCEPTION);
		Comment comment = commentRepository.findById(id)
			.orElseThrow(() -> CommentNotFoundException.EXCEPTION);

		checkDeleteAuthority(member, comment);

		comment.deleteCommentSoftlyWithContent();
	}

	private void checkDeleteAuthority(Member member, Comment comment) {
		if (comment.getMember() == member) {
			return;
		}
		if (member.getGrade() == Grade.EXECUTIVE || member.getGrade() == Grade.PRESIDENT) {
			return;
		}

		throw AccessForbiddenException.EXCEPTION;
	}

}
