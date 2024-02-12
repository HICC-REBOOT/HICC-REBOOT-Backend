package hiccreboot.backend.domains.comment.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.common.exception.AccessForbiddenException;
import hiccreboot.backend.common.exception.ArticleNotFoundException;
import hiccreboot.backend.common.exception.CommentNotFoundException;
import hiccreboot.backend.common.exception.MemberNotFoundException;
import hiccreboot.backend.domains.article.domain.Article;
import hiccreboot.backend.domains.article.repository.ArticleRepository;
import hiccreboot.backend.domains.comment.domain.Comment;
import hiccreboot.backend.domains.comment.domain.CommentGrade;
import hiccreboot.backend.domains.comment.dto.request.PostCommentRequest;
import hiccreboot.backend.domains.comment.dto.response.ChildCommentResponse;
import hiccreboot.backend.domains.comment.dto.response.ParentCommentResponse;
import hiccreboot.backend.domains.comment.repository.CommentRepository;
import hiccreboot.backend.domains.member.domain.Grade;
import hiccreboot.backend.domains.member.domain.Member;
import hiccreboot.backend.domains.member.repository.MemberRepository;
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

		List<ParentCommentResponse> parentCommentResponses = findParentComments(articleId).stream()
			.map((parentComment) -> new ParentCommentResponse(parentComment, parentComment.getMember() == member))
			.toList();

		return DataResponse.ok(parentCommentResponses);
	}

	public DataResponse<List<ChildCommentResponse>> makeChildComments(Long articleId, String studentNumber) {
		Member member = memberRepository.findByStudentNumber(studentNumber)
			.orElseThrow(() -> MemberNotFoundException.EXCEPTION);

		List<ChildCommentResponse> childCommentResponses = findChildComments(articleId).stream()
			.map((childComment) -> new ChildCommentResponse(childComment, childComment.getMember() == member))
			.toList();

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
