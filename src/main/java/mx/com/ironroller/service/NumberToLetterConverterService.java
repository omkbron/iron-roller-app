package mx.com.ironroller.service;

import java.math.BigDecimal;

import mx.gob.sat.sitio_internet.cfd.catalogos.CMoneda;

public interface NumberToLetterConverterService {

    String obtenerImporteLetra(BigDecimal total, CMoneda moneda);

}
