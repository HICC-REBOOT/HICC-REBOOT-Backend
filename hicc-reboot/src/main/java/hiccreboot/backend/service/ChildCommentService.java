package hiccreboot.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hiccreboot.backend.common.dto.BaseResponse;
import hiccreboot.backend.common.dto.ChildComment.ChildCommentResponse;
import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.common.exception.ArticleNotFoundException;
import hiccreboot.backend.common.exception.MemberNotFoundException;
import hiccreboot.backend.domain.Article;
import hiccreboot.backend.domain.ChildComment;
import hiccreboot.backend.domain.Member;
import hiccreboot.backend.repository.Article.ArticleRepository;
import hiccreboot.backend.repository.Comment.ChildCommentRepository;
import hiccreboot.backend.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChildCommentService {

	private final ChildCommentRepository childCommentRepository;
	private final MemberRepository memberRepository;
	private final ArticleRepository articleRepository;

	public List<ChildComment> findChildComments(Long articleId) {
		return childCommentRepository.findAllByArticle_Id(articleId);
	}

	public BaseResponse makeChildComments(Long articleId) {

		List<ChildComment> childComments = findChildComments(articleId);

		List<ChildCommentResponse> childCommentResponses = new ArrayList<>();
		childComments.stream()
			.forEach((childComment) -> childCommentResponses.add(new ChildCommentResponse(
				articleId,
				childComment.getParentCommentId(),
				childComment.getId(),
				childComment.getMember().getName(),
				childComment.getDate(),
				childComment.getContent())));
		return DataResponse.ok(childCommentResponses);
	}

	@Transactional
	public ChildComment saveChildComment(String studentNumber, Long articleId, Long parentCommentId, String content) {
		Member member = memberRepository.findByStudentNumber(studentNumber).orElseThrow(() ->
			MemberNotFoundException.EXCEPTION);
		Article article = articleRepository.findById(articleId).orElseThrow(() -> ArticleNotFoundException.EXCEPTION);

		ChildComment childComment = ChildComment.createChildComment(member, article, parentCommentId, content);

		return childCommentRepository.save(childComment);
	}

	@Transactional
	public void deleteChildComment(Long id) {
		childCommentRepository.deleteById(id);
	}
}
