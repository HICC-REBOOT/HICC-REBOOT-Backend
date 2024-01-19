package hiccreboot.backend.repository.Comment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hiccreboot.backend.domain.ParentComment;

public interface ParentCommentRepository extends JpaRepository<ParentComment, Long> {
	List<ParentComment> findAllByArticle_Id(Long id);
}
