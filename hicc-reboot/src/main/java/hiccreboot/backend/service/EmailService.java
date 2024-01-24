package hiccreboot.backend.service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.stream.IntStream;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import hiccreboot.backend.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

	private final JavaMailSender javaMailSender;
	private final MemberRepository memberRepository;

	private static final String SUBJECT = "HICC password reissue";
	private static final String CONTENT = "Temporary password is reissued. Change your password. Reissued Password: ";

	public void sendTempPassword(String studentNumber, String email) {
		memberRepository.findByStudentNumber(studentNumber)
			.ifPresent(member -> {
				if (member.getEmail().equals(email)) {
					String reissuedPassword = makeTempNumber();

					SimpleMailMessage mail = createEmail(member.getEmail(), reissuedPassword);

					javaMailSender.send(mail);
				}
			});

	}

	private SimpleMailMessage createEmail(String receiver, String reissuedPassword) {

		SimpleMailMessage message = new SimpleMailMessage();

		message.setTo(receiver);
		message.setSubject(SUBJECT);
		message.setText(CONTENT + reissuedPassword);

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
