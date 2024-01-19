package hiccreboot.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hiccreboot.backend.common.dto.BaseResponse;
import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.common.dto.ParentComment.ParentCommentResponse;
import hiccreboot.backend.common.exception.ArticleNotFoundException;
import hiccreboot.backend.common.exception.MemberNotFoundException;
import hiccreboot.backend.domain.Article;
import hiccreboot.backend.domain.Member;
import hiccreboot.backend.domain.ParentComment;
import hiccreboot.backend.repository.Article.ArticleRepository;
import hiccreboot.backend.repository.Comment.ParentCommentRepository;
import hiccreboot.backend.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParentCommentService {

	private final ParentCommentRepository parentCommentRepository;
	private final ArticleRepository articleRepository;
	private final MemberRepository memberRepository;

	public List<ParentComment> findParentComments(Long articleId) {
		return parentCommentRepository.findAllByArticle_Id(articleId);
	}

	public BaseResponse makeParentComments(Long articleId) {

		List<ParentComment> parentComments = findParentComments(articleId);

		List<ParentCommentResponse> parentCommentResponses = new ArrayList<>();
		parentComments.stream()
			.forEach((parentComment) -> parentCommentResponses.add(new ParentCommentResponse(
				articleId,
				parentComment.getId(),
				parentComment.getMember().getName(),
				parentComment.getDate(),
				parentComment.getContent())));
		return DataResponse.ok(parentComments);
	}

	@Transactional
	public ParentComment saveParentComment(Long articleId, String studentNumber, String content) {
		Article article = articleRepository.findById(articleId).orElseThrow(() -> ArticleNotFoundException.EXCEPTION);
		Member member = memberRepository.findByStudentNumber(studentNumber)
			.orElseThrow(() -> MemberNotFoundException.EXCEPTION);

		ParentComment parentComment = ParentComment.createParentComment(member, article, content);

		return parentCommentRepository.save(parentComment);
	}

	@Transactional
	public void deleteParentComment(Long id) {
		parentCommentRepository.deleteById(id);
	}
}
