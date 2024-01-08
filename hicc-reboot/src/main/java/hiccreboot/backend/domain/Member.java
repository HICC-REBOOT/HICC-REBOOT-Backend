package hiccreboot.backend.domain;

import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

	@Id
	@Column(name = "member_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String studentNumber;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Grade grade;

	@Column(nullable = false)
	private String phoneNumber;

	private String refreshToken;

	@Builder
	private Member(String studentNumber, String name, String password, Grade grade, String phoneNumber) {
		this.studentNumber = studentNumber;
		this.name = name;
		this.password = password;
		this.grade = grade;
		this.phoneNumber = phoneNumber;
	}

	public static Member createMember(String studentNumber, String name, String password, Grade grade,
		String phoneNumber) {
		return Member.builder()
			.studentNumber(studentNumber)
			.name(name)
			.password(password)
			.grade(grade)
			.phoneNumber(phoneNumber)
			.build();
	}

	public void updateGrade(Grade grade) {
		this.grade = grade;
	}

	public void passwordEncode(PasswordEncoder passwordEncoder) {
		this.password = passwordEncoder.encode(this.password);
	}

	public void updateRefreshToken(String updateRefreshToken) {
		this.refreshToken = updateRefreshToken;
	}

	public void destroyRefreshToken() {
		this.refreshToken = null;
	}

}
