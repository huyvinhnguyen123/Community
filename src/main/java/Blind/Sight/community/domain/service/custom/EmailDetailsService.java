package Blind.Sight.community.domain.service.custom;

import Blind.Sight.community.dto.email.EmailDetails;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailDetailsService {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    public String systemMail;

    public void sendSingleMailOTPWithTemplate(EmailDetails emailDetails, String newCode, Integer timeRemain) {
        try {
            // Creating a mime message
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper;
            // Create mime message helper to send mail
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(systemMail);
            mimeMessageHelper.setTo(emailDetails.getRecipient());

            // Create a Thymeleaf context
            Context context = new Context();
            context.setVariable("newCode", newCode);
            context.setVariable("timeSpecify", timeRemain);

            // Process the Thymeleaf template to generate HTML content
            String htmlContent = templateEngine.process("email-template", context);
            emailDetails.setMsgBody(htmlContent);

            mimeMessageHelper.setText(emailDetails.getMsgBody(), true); // when set true this mean you can custom this text by html
            mimeMessageHelper.setSubject(emailDetails.getSubject());
            javaMailSender.send(mimeMessage);
            log.info("Send mail SUCCESS!");

        } catch(MessagingException e) {
            log.error("Send mail FAIL!", e);
            log.trace("Make sure that your mail body is input and other mail property it's too!");
        }
    }

    public void sendMultipleMailsWithTemplate(EmailDetails emailDetails, String newCode, Integer timeRemain) {
        try {
            // Creating a mime message
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper;
            // Create mime message helper to send mail
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(systemMail);
            mimeMessageHelper.setTo(emailDetails.getRecipients());

            // Create a Thymeleaf context
            Context context = new Context();
            context.setVariable("newCode", newCode);
            context.setVariable("timeSpecify", timeRemain);

            // Process the Thymeleaf template to generate HTML content
            String htmlContent = templateEngine.process("email-template", context);
            emailDetails.setMsgBody(htmlContent);

            mimeMessageHelper.setText(emailDetails.getMsgBody(), true); // when set true this mean you can custom this text by html
            mimeMessageHelper.setSubject(emailDetails.getSubject());
            javaMailSender.send(mimeMessage);
            log.info("Send mail SUCCESS!");

        } catch(MessagingException e) {
            log.error("Send mail FAIL!", e);
            log.trace("Make sure that your mail body is input and other mail property it's too!");
        }
    }
}
