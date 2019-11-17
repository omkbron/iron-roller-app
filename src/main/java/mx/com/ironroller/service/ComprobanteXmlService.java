package mx.com.ironroller.service;

import java.io.InputStream;

import mx.gob.sat.cfd._3.Comprobante;
import mx.gob.sat.timbrefiscaldigital.TimbreFiscalDigital;

public interface ComprobanteXmlService {

	InputStream convierteComprobanteAStream(Comprobante comprobante);

	byte[] convierteComprobanteAByteArray(Comprobante comprobante);
	
	byte[] convierteComprobanteAByteArray(Comprobante comprobante, String encoding);

	Comprobante convierteByteArrayAComprobante(byte[] xmlCfdi);
	
	TimbreFiscalDigital obtenerComplementoTimbreFiscal(Comprobante comprobante);

	void imprimeEnConsola(Comprobante comprobante);

}
