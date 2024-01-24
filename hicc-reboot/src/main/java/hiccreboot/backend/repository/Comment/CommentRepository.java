package hiccreboot.backend.repository.Comment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hiccreboot.backend.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	List<Comment> findAllByArticle_IdAndParentCommentIdIsNull(Long id);

	List<Comment> findAllByArticle_IdAndParentCommentIdNotNull(Long id);
}
