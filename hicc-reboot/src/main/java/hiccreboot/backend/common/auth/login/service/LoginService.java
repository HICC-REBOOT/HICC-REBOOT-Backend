package hiccreboot.backend.common.auth.login.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hiccreboot.backend.common.exception.MemberNotFoundException;
import hiccreboot.backend.domains.member.domain.Member;
import hiccreboot.backend.domains.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

	private final MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Member member = findMemberByStudentNumber(username);
		return User.builder()
			.username(member.getStudentNumber())
			.password(member.getPassword())
			.roles(member.getGrade().getDescription())
			.build();
	}

	private Member findMemberByStudentNumber(String studentNumber) {
		return memberRepository.findByStudentNumber(studentNumber)
			.orElseThrow(() -> MemberNotFoundException.EXCEPTION);
	}
}
