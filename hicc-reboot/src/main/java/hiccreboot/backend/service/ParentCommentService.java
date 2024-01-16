package hiccreboot.backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hiccreboot.backend.domain.Article;
import hiccreboot.backend.domain.Member;
import hiccreboot.backend.domain.ParentComment;
import hiccreboot.backend.repository.Comment.ParentCommentRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParentCommentService {

	private final ParentCommentRepository parentCommentRepository;

	public List<ParentComment> findParentComments(Long articleId) {
		return parentCommentRepository.findAllByArticle_Id(articleId);
	}

	@Transactional
	public ParentComment saveParentComment(Article article, Member member, LocalDateTime date, String content) {
		ParentComment parentComment = ParentComment.createParentComment(date, content);
		parentComment.addArticle(article);
		parentComment.addMember(member);

		return parentCommentRepository.save(parentComment);
	}

	@Transactional
	public void deleteParentComment(Long id) {
		parentCommentRepository.deleteById(id);
	}
}
