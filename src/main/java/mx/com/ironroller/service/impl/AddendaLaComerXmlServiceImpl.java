package mx.com.ironroller.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;

import mx.com.ironroller.model.amece71.RequestForPayment;
import mx.com.ironroller.service.AddendaLaComerXmlService;
import mx.gob.sat.cfd._4.Comprobante;

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

    @Override
    public byte[] agregaAddenda(byte[] xmlCfdi, byte[] xmlAddenda) {
        SAXBuilder builder = new SAXBuilder();
        try {
            Document documentoCFD = (Document) builder.build(new ByteArrayInputStream(xmlAddenda));
            Document documentoCFDI = (Document) builder.build(new ByteArrayInputStream(xmlCfdi));
            Element addenda = new Element("Addenda", Namespace.getNamespace("cfdi", "http://www.sat.gob.mx/cfd/3"));
            addenda.addContent((Content) documentoCFD.getRootElement().clone());

            if (documentoCFDI.getRootElement().getChild("Addenda",
                    Namespace.getNamespace("cfdi", "http://www.sat.gob.mx/cfd/3")) != null) {
                documentoCFDI.getRootElement().removeChild("Addenda",
                        Namespace.getNamespace("cfdi", "http://www.sat.gob.mx/cfd/3"));
            }

            documentoCFDI.getRootElement().addContent((Element) addenda.clone());
            log.info("addenda agregada");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            XMLOutputter outputter = new XMLOutputter();
            outputter.setFormat(Format.getPrettyFormat().setEncoding("UTF-8"));
            // outputter.setFormat(Format.getCompactFormat().setEncoding("UTF-8"));
            outputter.output(documentoCFDI, baos);
            return baos.toByteArray();
        } catch (Exception e) {
            log.error("Ocurrio un error al agregar la addenda.", e);
            return null;
        }
    }
}
