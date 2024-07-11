package mx.com.ironroller.service;

import mx.gob.sat.cfd._4.Comprobante;

public interface FacturaService {

    Comprobante procesarXml(byte[] xml);

}
