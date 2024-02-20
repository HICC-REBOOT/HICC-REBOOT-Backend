package hiccreboot.backend.domains.member.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hiccreboot.backend.domains.member.domain.Grade;
import hiccreboot.backend.domains.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByStudentNumber(String studentNumber);

	List<Member> findAllByGrade(Grade grade);

	Page<Member> findAllByGrade(Grade grade, Pageable pageable);

	@Query(value = "select m "
		+ "from Member m "
		+ "where ( (m.name like concat('%',:name, '%')) AND (m.grade <> 'APPLICANT'))")
	Page<Member> findAllByNameWithoutApplicant(@Param("name") String name, Pageable pageable);

	Long countByGradeNot(Grade grade);
}
