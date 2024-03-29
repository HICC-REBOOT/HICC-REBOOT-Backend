package hiccreboot.backend.domains.member.domain;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;

import hiccreboot.backend.domains.department.domain.Department;
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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

	@Id
	@Column(name = "MEMBER_ID")
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

	private LocalDateTime approvedDate;

	@Column(nullable = false)
	private String email;

	@Builder(access = AccessLevel.PRIVATE)
	private Member(String studentNumber, Department department, String name, String password, Grade grade,
		String phoneNumber, String email) {
		this.studentNumber = studentNumber;
		this.department = department;
		this.name = name;
		this.password = password;
		this.grade = grade;
		this.phoneNumber = phoneNumber;
		this.email = email;
	}

	public static Member signUp(String studentNumber, Department department, String name, String password,
		String phoneNumber, String email) {
		return Member.builder()
			.studentNumber(studentNumber)
			.department(department)
			.name(name)
			.password(password)
			.grade(Grade.APPLICANT)
			.phoneNumber(phoneNumber)
			.email(email)
			.build();
	}

	public void updateGrade(Grade grade) {
		this.grade = grade;
	}

	public void updatePhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void updateDepartment(Department department) {
		this.department = department;
	}

	public void updatePassword(String password) {
		this.password = password;
	}

	public void updateEmail(String email) {
		this.email = email;
	}

	public void passwordEncode(PasswordEncoder passwordEncoder) {
		this.password = passwordEncoder.encode(this.password);
	}

	public void approve() {
		this.grade = Grade.NORMAL;
		this.approvedDate = LocalDateTime.now();
	}

}
