package com.mdn.backend.auth.forgotpassword;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    private void sendEmail(String email, String subject, String messageContent) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(messageContent, true);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendVerificationCode(String email, String verificationCode) {
        String subject = "Verification Code";
        String htmlContent = generateEmailHtmlContent(verificationCode, "reset your password");
        sendEmail(email, subject, htmlContent);
    }

    public void sendEmailVerification(String email, String verificationCode) {
        String subject = "Email Verification";
        String htmlContent = generateEmailHtmlContent(verificationCode, "verify your email");
        sendEmail(email, subject, htmlContent);
    }

    private String generateEmailHtmlContent(String verificationCode, String actionDescription) {
        return "<html><body>" +
                "<p>Hello,</p>" +
                "<p>Your verification code is: <strong>" + verificationCode + "</strong></p>" +
                "<p>Please use this code to " + actionDescription + ".</p>" +
                "</body></html>";
    }
}
