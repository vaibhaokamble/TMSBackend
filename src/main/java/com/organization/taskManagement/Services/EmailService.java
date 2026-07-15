package com.organization.taskManagement.Services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username:noreply@company.com}")
    private String fromEmail;

    public void sendOtpEmail(String toEmail, String employeeName, String otp, String purpose) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Your OTP for " + purpose + " - Task Management System");

            String htmlContent = generateHtmlTemplate(employeeName, otp, purpose);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException | org.springframework.mail.MailException e) {
            logger.error("Failed to send HTML email. Root cause: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to send email: Please verify SMTP configuration or email address.");
        }
    }

    private String generateHtmlTemplate(String employeeName, String otp, String purpose) {
        return "<!DOCTYPE html>" +
               "<html>" +
               "<head>" +
               "<meta charset='UTF-8'>" +
               "<style>" +
               "  body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f7f6; margin: 0; padding: 0; }" +
               "  .container { max-width: 600px; margin: 40px auto; background-color: #ffffff; border-radius: 8px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); overflow: hidden; }" +
               "  .header { background-color: #4f46e5; padding: 30px 20px; text-align: center; color: white; }" +
               "  .header h1 { margin: 0; font-size: 24px; font-weight: 600; }" +
               "  .content { padding: 40px 30px; color: #333333; line-height: 1.6; }" +
               "  .greeting { font-size: 18px; font-weight: 500; margin-bottom: 20px; }" +
               "  .otp-box { background-color: #f8fafc; border: 2px dashed #4f46e5; border-radius: 8px; padding: 20px; text-align: center; margin: 30px 0; }" +
               "  .otp-code { font-size: 32px; font-weight: bold; color: #4f46e5; letter-spacing: 5px; margin: 0; }" +
               "  .expiry { color: #dc2626; font-size: 14px; text-align: center; margin-top: 10px; }" +
               "  .footer { background-color: #f8fafc; padding: 20px; text-align: center; font-size: 12px; color: #64748b; border-top: 1px solid #e2e8f0; }" +
               "</style>" +
               "</head>" +
               "<body>" +
               "  <div class='container'>" +
               "    <div class='header'>" +
               "      <h1>Task Management System</h1>" +
               "    </div>" +
               "    <div class='content'>" +
               "      <div class='greeting'>Hello " + employeeName + ",</div>" +
               "      <p>We received a request for <strong>" + purpose + "</strong>. Please use the following One-Time Password (OTP) to complete your request:</p>" +
               "      <div class='otp-box'>" +
               "        <p class='otp-code'>" + otp + "</p>" +
               "      </div>" +
               "      <p class='expiry'>⏳ This OTP is valid for exactly <strong>5 minutes</strong>. Do not share this code with anyone.</p>" +
               "      <p>If you did not request this, please ignore this email or contact support.</p>" +
               "    </div>" +
               "    <div class='footer'>" +
               "      &copy; 2026 KoderzTech - Task Management System. All rights reserved.<br>" +
               "      Need help? Contact us at support@koderztech.com" +
               "    </div>" +
               "  </div>" +
               "</body>" +
               "</html>";
    }
}
