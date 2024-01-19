package hiccreboot.backend.repository.member;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import hiccreboot.backend.domain.Grade;
import hiccreboot.backend.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByStudentNumber(String studentNumber);

	Page<Member> findAllByGrade(Grade grade, Pageable pageable);
}
