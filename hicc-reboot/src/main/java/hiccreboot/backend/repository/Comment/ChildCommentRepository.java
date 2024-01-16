package hiccreboot.backend.repository.Comment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hiccreboot.backend.domain.ChildComment;

public interface ChildCommentRepository extends JpaRepository<ChildComment, Long> {
	List<ChildComment> findAllByArticle_Id(Long id);
}
