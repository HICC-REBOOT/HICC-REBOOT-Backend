package hiccreboot.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hiccreboot.backend.common.dto.BaseResponse;
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

	public BaseResponse makeParentComments(Long articleId, String studentNumber) {
		Member member = memberRepository.findByStudentNumber(studentNumber)
			.orElseThrow(() -> MemberNotFoundException.EXCEPTION);

		List<Comment> parentComments = findParentComments(articleId);

		List<ParentCommentResponse> parentCommentResponses = new ArrayList<>();
		parentComments.stream()
			.forEach((parentComment) -> parentCommentResponses.add(new ParentCommentResponse(
				parentComment,
				parentComment.getMember() == member)));

		return DataResponse.ok(parentCommentResponses);
	}

	public BaseResponse makeChildComments(Long articleId, String studentNumber) {
		Member member = memberRepository.findByStudentNumber(studentNumber)
			.orElseThrow(() -> MemberNotFoundException.EXCEPTION);

		List<Comment> childComments = findChildComments(articleId);

		List<ChildCommentResponse> childCommentResponses = new ArrayList<>();
		childComments.stream()
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

		Comment comment;
		if (postCommentRequest.getParentCommentId() > PARENT_COMMENT) {
			comment = Comment.createChildComment(postCommentRequest.getParentCommentId(), member, article,
				postCommentRequest.getContent());
		} else {
			comment = Comment.createParentComment(member, article, postCommentRequest.getContent());
		}

		return commentRepository.save(comment);
	}

	@Transactional
	public void deleteComment(Long id, String studentNumber) {
		Member member = memberRepository.findByStudentNumber(studentNumber)
			.orElseThrow(() -> MemberNotFoundException.EXCEPTION);
		Comment comment = commentRepository.findById(id)
			.orElseThrow(() -> CommentNotFoundException.EXCEPTION);

		if (comment.getMember() != member) {
			throw AccessForbiddenException.EXCEPTION;
		}

		commentRepository.deleteById(id);
	}
}
