package org.restro.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.restro.entity.User;
import org.restro.entity.WeeklyEmail;
import org.restro.repository.UserRepository;
import org.restro.repository.WeeklyEmailRepository;
import org.restro.service.SendMailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SendMailServiceImpl implements SendMailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final WeeklyEmailRepository weeklyEmailRepository;
    private final UserRepository userRepository;

    @Override
    @Async
    public void send(String to, String subject, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        javaMailSender.send(simpleMailMessage);
    }

    @Override
    @Async
    public void sendWelcomeMail(User user) {

        final Context ctx = new Context();
        ctx.setVariable("user", user);

        final String htmlContent = templateEngine.process("mail/welcomeMail.html", ctx);

        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setSubject("Welcome Html Mail");
            message.setTo(user.getEmail());

            message.setText(htmlContent, true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Async
    @Scheduled(cron = "0 0 0 * * 0")
    public void sendWeeklyEmail() {
        List<WeeklyEmail> all = weeklyEmailRepository.findAll();

        for (WeeklyEmail email : all) {
            send(email.getEmail(), "Weekly Email", "Hi, You will get this email every week.");
        }

    }


}
