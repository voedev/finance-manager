package com.voedev.notificationsender.service.impl;

import com.voedev.notificationsender.config.mailjet.MailjetProperties;
import com.voedev.notificationsender.model.event.WelcomeEmailEvent;
import com.voedev.notificationsender.service.EmailSender;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
// todo refactoring
public class EmailSenderImpl implements EmailSender {

    private final JavaMailSender javaMailSender;
    private final MailjetProperties mailjetProperties;

    @Override
    public void send(WelcomeEmailEvent emailEvent) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

            mimeMessageHelper.setTo(emailEvent.getEmail());
            mimeMessageHelper.setFrom(mailjetProperties.getUsername());
            mimeMessageHelper.setSubject("Welcome to MoneyMap");
            mimeMessageHelper.setText(getWelcomeEmailContent(emailEvent.getEmail()), true);

            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("Error sending email", e);
        }
    }

    private String getWelcomeEmailContent(String email) {
        return "<html><body>" +
                "<h2>Добро пожаловать в наш сервис!</h2>" +
                "<p>Уважаемый пользователь " + email + ",</p>" +
                "<p>Мы рады приветствовать вас в нашем сервисе. Благодарим за регистрацию!</p>" +
                "<p>С уважением,<br>Команда сервиса</p>" +
                "</body></html>";
    }
}
