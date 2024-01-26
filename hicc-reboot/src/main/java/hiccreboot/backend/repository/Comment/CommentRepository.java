package hiccreboot.backend.repository.Comment;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import hiccreboot.backend.domain.Comment;
import hiccreboot.backend.domain.Member;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	List<Comment> findAllByArticle_IdAndParentCommentIdGreaterThan(Long id, Long parentCommentId);

	List<Comment> findAllByArticle_IdAndParentCommentIdNotNull(Long id);

	Page<Comment> findAllByMember(Member member, Pageable pageable);
	List<Comment> findAllByArticle_IdAndParentCommentIdLessThanEqual(Long id, Long parentCommentId);
}
