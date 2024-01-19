package hiccreboot.backend.repository.member;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hiccreboot.backend.domain.Grade;
import hiccreboot.backend.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByStudentNumber(String studentNumber);

	Page<Member> findAllByGrade(Grade grade, Pageable pageable);

	@Query(value = "select m "
		+ "from Member m "
		+ "where ( (m.name like concat('%',:name, '%')) AND (m.grade <> 'APPLICANT'))")
	Page<Member> findAllByNameWithoutApplicant(@Param("name") String name, Pageable pageable);
}
