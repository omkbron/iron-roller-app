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

import mx.com.ironroller.service.ComprobanteXmlService;
import mx.gob.sat.cfd._3.Comprobante;
import mx.gob.sat.cfd._3.Comprobante.Complemento;
import mx.gob.sat.timbrefiscaldigital.TimbreFiscalDigital;

@Service
public class ComprobanteXmlServiceImpl extends XmlService implements ComprobanteXmlService {
    private Logger log = LoggerFactory.getLogger(ComprobanteXmlServiceImpl.class);

    @Autowired
    @Qualifier(value = "cfdiMarshaller")
    private Jaxb2Marshaller cfdiMarshaller;

    @Override
    public InputStream convierteComprobanteAStream(Comprobante comprobante) {
        return new ByteArrayInputStream(convierteComprobanteAByteArray(comprobante, "UTF-8"));
    }

    @Override
    public byte[] convierteComprobanteAByteArray(Comprobante comprobante) {
        return convierteComprobanteAByteArray(comprobante, "UTF-8");
    }

    @Override
    public byte[] convierteComprobanteAByteArray(Comprobante comprobante, String encoding) {
        Jaxb2Marshaller marshaller = getMarshaller(comprobante);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter oos;
        try {
            oos = new OutputStreamWriter(baos, encoding);
            marshaller.marshal(comprobante, new StreamResult(oos));
            oos.flush();
            oos.close();
        } catch (UnsupportedEncodingException e) {
            log.error("Error en el encoding", e);
        } catch (IOException e) {
            log.error("Error al convertir el comprobante", e);
        }
        return baos.toByteArray();
    }

    private Jaxb2Marshaller getMarshaller(Comprobante comprobante) {
        if (comprobante == null) {
            throw new RuntimeException("Ocurrio un error al obtener el complemento, el comprobante no es valido.");
        }
        return cfdiMarshaller;
    }

    @Override
    public void imprimeEnConsola(Comprobante comprobante) {
        try {
            Jaxb2Marshaller marshaller = getMarshaller(comprobante);
            marshaller.marshal(comprobante, new StreamResult(System.out));
        } catch (Exception e) {
            log.error("Error al convertir el comprobante para impresion en consola {}", e.getMessage());
        }
    }

    @Override
    public Comprobante convierteByteArrayAComprobante(byte[] xmlCfdi) {
        return (Comprobante) cfdiMarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xmlCfdi)));
    }

    @Override
    public TimbreFiscalDigital obtenerComplementoTimbreFiscal(Comprobante comprobante) {
        if (comprobante != null) {
            for (Complemento comprobanteComplemento : comprobante.getComplemento()) {
                for (Object complemento : comprobanteComplemento.getAny()) {
                    if (complemento instanceof TimbreFiscalDigital) {
                        return (TimbreFiscalDigital) complemento;
                    }
                }
            }
        }
        return null;
    }
}