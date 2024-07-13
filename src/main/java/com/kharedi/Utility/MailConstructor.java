package com.kharedi.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.kharedi.model.OrderedData;
import com.kharedi.model.User;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class MailConstructor {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendHtmlEmail(User user, OrderedData order) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        String message = "<html><body>"
                + "<p>You purchased <strong>" + order.getProductName() + "</strong> and the total price is <strong>" + order.getTotalPrice() + "</strong></p>"
                + "<p>Ordered Date: <strong>" + order.getOrderDate() + "</strong> & Delivered Date: <strong>" + order.getConfirmOrderDate() + "</strong></p>"
                + "<p>Buy More products.</p>"
                + "</body></html>";

        try {
            helper.setTo(user.getEmail());
            helper.setSubject("Thank you for purchasing products from Kharedi");
            helper.setText(message, true); // Set true to indicate HTML content
            helper.setFrom("projecttybsc35@gmail.com");

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }
}
