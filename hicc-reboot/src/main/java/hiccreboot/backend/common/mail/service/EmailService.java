package hiccreboot.backend.common.mail.service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.stream.IntStream;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import hiccreboot.backend.common.exception.EmailMismatchException;
import hiccreboot.backend.common.exception.MemberNotFoundException;
import hiccreboot.backend.domains.auth.domain.PasswordReset;
import hiccreboot.backend.domains.auth.repository.PasswordResetRepository;
import hiccreboot.backend.domains.member.domain.Member;
import hiccreboot.backend.domains.member.repository.MemberRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EmailService {

	private final JavaMailSender javaMailSender;
	private final MemberRepository memberRepository;
	private final PasswordResetRepository passwordResetRepository;
	private final SpringTemplateEngine springTemplateEngine;
	private final MimeMessageHelper helper;

	private static final String SUBJECT = "HICC password reissue";
	private static final String NONCE = "NONCE";
	private static final String PASSWORD_EMAIL_TEMPLATE = "index";

	public void sendNonce(String studentNumber, String email) {
		Member member = memberRepository.findByStudentNumber(studentNumber)
			.filter(targetMember -> validateEmail(targetMember, email))
			.orElseThrow(() -> MemberNotFoundException.EXCEPTION);

		String nonce = makeTempNumber();
		MimeMessage mail = createEmail(member.getEmail(), nonce);

		passwordResetRepository.save(PasswordReset.create(member, nonce));
		javaMailSender.send(mail);
	}

	private boolean validateEmail(Member member, String email) {
		if (member.getEmail().equals(email)) {
			return true;
		}
		throw EmailMismatchException.EXCEPTION;
	}

	private MimeMessage createEmail(String receiver, String reissuedPassword) {
		try {
			helper.setTo(receiver);
			helper.setSubject(SUBJECT);

			Context context = new Context();
			context.setVariable(NONCE, reissuedPassword);
			String html = springTemplateEngine.process(PASSWORD_EMAIL_TEMPLATE, context);
			helper.setText(html, true);

			return helper.getMimeMessage();
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

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
