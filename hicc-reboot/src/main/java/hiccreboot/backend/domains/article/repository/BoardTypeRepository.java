package hiccreboot.backend.domains.article.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hiccreboot.backend.domains.article.domain.BoardTypeEntity;

public interface BoardTypeRepository extends JpaRepository<BoardTypeEntity, Long> {
}
