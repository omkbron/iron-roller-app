package mx.com.ironroller.service.impl;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import mx.com.ironroller.service.MailService;

@Service
public class MailServiceImpl implements MailService {

    private final Logger log = LoggerFactory.getLogger(MailServiceImpl.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendMail(String message, String subject, String... recipients) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setText(message);
            msg.setSubject(subject);
            msg.setFrom(from);
            msg.setTo(recipients);
            javaMailSender.send(msg);
            log.info("Se ha enviado el correo de manera correcta.");
        } catch (MailException e) {
            log.error("Error al enviar el correo {}", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void sendMimeMail(String message, String messageHtml, String subject, String... recipients) {
        MimeMessage msg = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
            helper.setText(message, messageHtml);
            helper.setSubject(subject);
            helper.setFrom(from);
            helper.setTo(recipients);
        } catch (MessagingException e) {
            log.error("Error al enviar el correo {}", e);
            throw new RuntimeException(e.getMessage());
        }
        javaMailSender.send(msg);
        log.info("Se ha enviado el correo de manera correcta.");
    }

    @Override
    public void sendMailWithAttach(String message, String messageHtml, String subject, Resource[] attach,
            String... recipients) {
        try {
            MimeMessage msg = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
            helper.setText(message, messageHtml);
            helper.setSubject(subject);
            helper.setFrom(from);
            helper.setTo(recipients);
            for (Resource att : attach) {
                helper.addAttachment(att.getFilename(), att);
            }
            javaMailSender.send(msg);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
