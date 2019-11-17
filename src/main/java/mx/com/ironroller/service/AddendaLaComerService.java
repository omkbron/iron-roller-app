package mx.com.ironroller.service;

import mx.com.ironroller.model.DatosAddenda;
import mx.com.ironroller.model.amece71.RequestForPayment;
import mx.gob.sat.cfd._3.Comprobante;

public interface AddendaLaComerService {

    RequestForPayment crear(Comprobante comprobante, DatosAddenda datosAddenda);

}
