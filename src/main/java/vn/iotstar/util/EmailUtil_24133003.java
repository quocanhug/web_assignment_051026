package vn.iotstar.util;

import java.util.Properties;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailUtil_24133003 {

    // Cấu hình Gmail SMTP
    private static final String FROM_EMAIL = vn.iotstar.connection.DBConnection_24133003.setting("SMTP_USER", "");
    private static final String APP_PASSWORD = vn.iotstar.connection.DBConnection_24133003.setting("SMTP_PASSWORD", "");

    public static boolean sendEmail(String toEmail, String subject, String body) {
        if (FROM_EMAIL.isBlank() || APP_PASSWORD.isBlank()) return false;
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL, "Web BookStore"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);
            System.out.println("Email đã gửi thành công tới: " + toEmail);
            return true;
        } catch (Exception e) {
            System.err.println("Lỗi gửi email tới: " + toEmail + " - " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
