package hiccreboot.backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hiccreboot.backend.domain.Article;
import hiccreboot.backend.domain.ChildComment;
import hiccreboot.backend.domain.Member;
import hiccreboot.backend.repository.Comment.ChildCommentRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChildCommentService {

	private ChildCommentRepository childCommentRepository;

	public List<ChildComment> findChildComments(Long articleId) {
		return childCommentRepository.findAllByArticle_Id(articleId);
	}

	@Transactional
	public ChildComment saveChildComment(Article article, Member member, LocalDateTime date, Long parentCommentId,
		String content) {
		ChildComment childComment = ChildComment.createChildComment(date, parentCommentId, content);
		childComment.addArticle(article);
		childComment.addMember(member);

		return childCommentRepository.save(childComment);
	}

	@Transactional
	public void deleteChildComment(Long id) {
		childCommentRepository.deleteById(id);
	}
}
