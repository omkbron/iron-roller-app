package mx.com.ironroller.service;

import org.springframework.core.io.Resource;

public interface MailService {

    void sendMail(String message, String subject, String... recipients);

    void sendMimeMail(String message, String messageHtml, String subject, String... recipients);

    void sendMailWithAttach(String message, String messageHtml, String subject, Resource[] attach, String... recipients);

}
