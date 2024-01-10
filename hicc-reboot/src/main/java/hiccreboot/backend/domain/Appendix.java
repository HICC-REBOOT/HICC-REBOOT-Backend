package hiccreboot.backend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Appendix {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "APPENDIX_ID")
    private Long id;

    @Column(nullable = false)
    private String path;

    @Builder
    private Appendix(String path) {
        this.path = path;
    }

    public Appendix createAppendix(String path) {
        return Appendix.builder()
                .path(path)
                .build();
    }
}
