package hiccreboot.backend.domains.comment.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import hiccreboot.backend.domains.comment.domain.Comment;
import hiccreboot.backend.domains.member.domain.Member;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	List<Comment> findAllByArticle_IdAndParentCommentIdGreaterThan(Long id, Long parentCommentId);

	Page<Comment> findAllByMember(Member member, Pageable pageable);

	List<Comment> findAllByMember(Member member);

	List<Comment> findAllByArticle_IdAndParentCommentIdLessThanEqual(Long id, Long parentCommentId);
}
