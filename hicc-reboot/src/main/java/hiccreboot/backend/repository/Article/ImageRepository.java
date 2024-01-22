package hiccreboot.backend.repository.Article;

import org.springframework.data.jpa.repository.JpaRepository;

import hiccreboot.backend.domain.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
