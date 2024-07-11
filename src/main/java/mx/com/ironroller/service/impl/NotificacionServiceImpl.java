package mx.com.ironroller.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import mx.com.ironroller.service.MailService;
import mx.com.ironroller.service.NotificacionService;
import mx.gob.sat.cfd._4.Comprobante;

@Service
public class NotificacionServiceImpl implements NotificacionService {
    private final Logger log = LoggerFactory.getLogger(NotificacionServiceImpl.class);

    @Autowired
    private MailService mailService;

    @Async
    @Override
    public void notificacionAddendaCreada(Comprobante comprobante) {
        log.info("notificacion addenda creada");
        try {
            String message = String.format(
                    "Se ha agregado una addenda a la factura %s-%s la cual es por un importe de $%s",
                    comprobante.getSerie(), comprobante.getFolio(), comprobante.getTotal());
            String messageHtml = "<p>" + message + "</p>";
            String subject = "Addenda creada";
            String[] recipients = { "omvp29@gmail.com" };
            mailService.sendMimeMail(message, messageHtml, subject, recipients);
        } catch (Exception ex) {
            log.error("Ocurrio un error al enviar la notificacion: " + ex.getMessage(), ex);
        }
    }
}
