package hiccreboot.backend.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEPARTMENT_ID")
	private Department department;

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
	private List<Article> articles = new ArrayList<>();

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
	private List<Comment> comments = new ArrayList<>();

	private String refreshToken;

	@Builder(access = AccessLevel.PRIVATE)
	private Member(String studentNumber, Department department, String name, String password, Grade grade,
		String phoneNumber) {
		this.studentNumber = studentNumber;
		this.department = department;
		this.name = name;
		this.password = password;
		this.grade = grade;
		this.phoneNumber = phoneNumber;
	}

	public static Member signUp(String studentNumber, Department department, String name, String password,
		String phoneNumber) {
		return Member.builder()
			.studentNumber(studentNumber)
			.department(department)
			.name(name)
			.password(password)
			.grade(Grade.APPLICANT)
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
