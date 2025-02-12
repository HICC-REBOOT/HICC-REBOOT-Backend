package hiccreboot.backend.domains.article.domain;

import static jakarta.persistence.GenerationType.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardTypeEntity {

	@Id
	@Column(name = "BOARD_TYPE_ID")
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	public BoardTypeEntity(String name) {
		this.name = name;
	}

	public void updateName(String name) {
		this.name = name;
	}
}
