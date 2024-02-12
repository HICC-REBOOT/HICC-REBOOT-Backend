package hiccreboot.backend.domains.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hiccreboot.backend.domains.image.domain.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
