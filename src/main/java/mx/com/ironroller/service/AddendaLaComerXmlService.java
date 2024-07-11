package mx.com.ironroller.service;

import java.io.InputStream;

import mx.com.ironroller.model.amece71.RequestForPayment;
import mx.gob.sat.cfd._4.Comprobante;

public interface AddendaLaComerXmlService {

	InputStream convierteAddendaEnStream(RequestForPayment requestForPayment);

	byte[] convierteAddendaEnByteArray(RequestForPayment requestForPayment);
	
	byte[] convierteAddendaEnByteArray(RequestForPayment requestForPayment, String encoding);

	Comprobante convierteByteArrayEnAddenda(byte[] xmlAddenda);

	void imprimeEnConsola(RequestForPayment requestForPayment);

    byte[] agregaAddenda(byte[] xmlCfdi, byte[] xmlAddenda);

}
