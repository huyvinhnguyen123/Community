package Blind.Sight.community.domain.service.custom;

import Blind.Sight.community.dto.email.EmailDetails;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
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
    private static final String SEND_SUCCESS = "Send mail success!";
    private static final String SEND_FAIL = "Send mail fail!";
    private static final String LOG_TRACE = "Make sure that your mail body is input and other mail property it's too!";

    @Value("${spring.mail.username}")
    public String systemMail;

    @Async
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
            context.setVariable("email", emailDetails.getRecipient());
            context.setVariable("newCode", newCode);
            context.setVariable("timeSpecify", timeRemain);

            // Process the Thymeleaf template to generate HTML content
            if (emailDetails.getSubject().equals("Reset Password")) {
                String htmlContent = templateEngine.process("email-template-reset-password.html", context);
                emailDetails.setMsgBody(htmlContent);
            }

            if (emailDetails.getSubject().equals("Unlock Account")) {
                String htmlContent = templateEngine.process("email-template-unlock-account.html", context);
                emailDetails.setMsgBody(htmlContent);
            }

            mimeMessageHelper.setText(emailDetails.getMsgBody(), true); // when set true this mean you can custom this text by html
            mimeMessageHelper.setSubject(emailDetails.getSubject());
            javaMailSender.send(mimeMessage);
            log.info(SEND_SUCCESS);

        } catch(MessagingException e) {
            log.error(SEND_FAIL, e);
            log.trace(LOG_TRACE);
        }
    }

    @Async
    public void sendMultipleMailsWithTemplate(EmailDetails emailDetails, String sku, String productName, Double price) {
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
            context.setVariable("sku", sku);
            context.setVariable("productName", productName);
            context.setVariable("price", price);

            // Process the Thymeleaf template to generate HTML content
            String htmlContent = templateEngine.process("email-template-update-product.html", context);
            emailDetails.setMsgBody(htmlContent);

            mimeMessageHelper.setText(emailDetails.getMsgBody(), true); // when set true this mean you can custom this text by html
            mimeMessageHelper.setSubject(emailDetails.getSubject());
            javaMailSender.send(mimeMessage);
            log.info(SEND_SUCCESS);

        } catch(MessagingException e) {
            log.error(SEND_FAIL, e);
            log.trace(LOG_TRACE);
        }
    }
}
