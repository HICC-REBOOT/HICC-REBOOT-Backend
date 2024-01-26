package hiccreboot.backend.service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.stream.IntStream;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hiccreboot.backend.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EmailService {

	private final JavaMailSender javaMailSender;
	private final MemberRepository memberRepository;
	private final SimpleMailMessage template;
	private final PasswordEncoder passwordEncoder;

	private static final String SUBJECT = "HICC password reissue";
	private static final String CONTENT = "임시 비밀번호가 발급되었습니다.\n반드시 비밀번호를 변경해주세요.\nReissued Password:%s";

	public void sendTempPassword(String studentNumber, String email) {
		memberRepository.findByStudentNumber(studentNumber)
			.filter(member -> member.getEmail().equals(email))
			.ifPresent(member -> {
				String reissuedPassword = makeTempNumber();

				SimpleMailMessage mail = createEmail(member.getEmail(), reissuedPassword);

				member.updatePassword(reissuedPassword);
				member.passwordEncode(passwordEncoder);
				javaMailSender.send(mail);
			});
	}

	private SimpleMailMessage createEmail(String receiver, String reissuedPassword) {

		SimpleMailMessage message = new SimpleMailMessage(template);

		message.setTo(receiver);
		message.setSubject(SUBJECT);
		String text = String.format(CONTENT, reissuedPassword);
		message.setText(text);

		return message;
	}

	private String makeTempNumber() {
		int length = 6;

		try {
			Random random = SecureRandom.getInstanceStrong();
			StringBuilder builder = new StringBuilder();

			IntStream.range(0, length)
				.forEach(avoid -> builder.append(random.nextInt(10)));

			return builder.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}
