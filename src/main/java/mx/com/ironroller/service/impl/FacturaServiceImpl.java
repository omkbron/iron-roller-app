package mx.com.ironroller.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.ironroller.model.ComprobanteInvalidoException;
import mx.com.ironroller.service.ComprobanteXmlService;
import mx.com.ironroller.service.FacturaService;
import mx.gob.sat.cfd._3.Comprobante;

@Service
public class FacturaServiceImpl implements FacturaService {
    private Logger log = LoggerFactory.getLogger(FacturaServiceImpl.class);

    @Autowired
    private ComprobanteXmlService comprobanteXmlService;

    @Override
    public Comprobante procesarXml(byte[] xml) {
        Comprobante comprobante = null;
        try {
            comprobante = comprobanteXmlService.convierteByteArrayAComprobante(xml);
        } catch (Exception ex) {
            log.error("Error al obtener el xml");
            throw new ComprobanteInvalidoException("Error al procesar el xml, sólo son válidos CFDI 3.3, favor de verificar.");
        }
        return comprobante;
    }

}
