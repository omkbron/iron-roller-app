package mx.com.ironroller.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;

import mx.com.ironroller.model.amece71.RequestForPayment;
import mx.com.ironroller.service.AddendaLaComerXmlService;
import mx.gob.sat.cfd._3.Comprobante;

@Service
public class AddendaLaComerXmlServiceImpl extends XmlService implements AddendaLaComerXmlService {
    private Logger log = LoggerFactory.getLogger(AddendaLaComerXmlServiceImpl.class);

    @Autowired
    @Qualifier(value = "addendaLaComerMarshaller")
    private Jaxb2Marshaller addendaLaComerMarshaller;

    @Override
    public InputStream convierteAddendaEnStream(RequestForPayment requestForPayment) {
        return new ByteArrayInputStream(convierteAddendaEnByteArray(requestForPayment, "UTF-8"));
    }

    @Override
    public byte[] convierteAddendaEnByteArray(RequestForPayment requestForPayment) {
        return convierteAddendaEnByteArray(requestForPayment, "UTF-8");
    }

    @Override
    public byte[] convierteAddendaEnByteArray(RequestForPayment requestForPayment, String encoding) {
        Jaxb2Marshaller marshaller = getMarshaller(requestForPayment);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter oos;
        try {
            oos = new OutputStreamWriter(baos, encoding);
            marshaller.marshal(requestForPayment, new StreamResult(oos));
            oos.flush();
            oos.close();
        } catch (UnsupportedEncodingException e) {
            log.error("Error en el encoding", e);
        } catch (IOException e) {
            log.error("Error al convertir el comprobante", e);
        }
        return baos.toByteArray();
    }

    private Jaxb2Marshaller getMarshaller(RequestForPayment requestForPayment) {
        if (requestForPayment == null) {
            throw new RuntimeException("Ocurrio un error al obtener la addenda, el objeto no es valido.");
        }
        return addendaLaComerMarshaller;
    }

    @Override
    public void imprimeEnConsola(RequestForPayment requestForPayment) {
        try {
            Jaxb2Marshaller marshaller = getMarshaller(requestForPayment);
            marshaller.marshal(requestForPayment, new StreamResult(System.out));
        } catch (Exception e) {
            log.error("Error al convertir la addenda para impresion en consola {}", e.getMessage());
        }
    }

    @Override
    public Comprobante convierteByteArrayEnAddenda(byte[] xmlAddenda) {
        return (Comprobante) addendaLaComerMarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xmlAddenda)));
    }
}