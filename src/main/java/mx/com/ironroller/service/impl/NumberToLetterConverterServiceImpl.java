package mx.com.ironroller.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.springframework.stereotype.Service;

import mx.com.ironroller.service.NumberToLetterConverterService;
import mx.gob.sat.sitio_internet.cfd.catalogos.CMoneda;

@Service
public class NumberToLetterConverterServiceImpl implements NumberToLetterConverterService {
    private static final String[] UNIDADES = { "", "UN ", "DOS ", "TRES ", "CUATRO ", "CINCO ", "SEIS ", "SIETE ",
            "OCHO ", "NUEVE ", "DIEZ ", "ONCE ", "DOCE ", "TRECE ", "CATORCE ", "QUINCE ", "DIECISEIS", "DIECISIETE",
            "DIECIOCHO", "DIECINUEVE", "VEINTE" };

    private static final String[] DECENAS = { "VEINTI", "TREINTA ", "CUARENTA ", "CINCUENTA ", "SESENTA ", "SETENTA ",
            "OCHENTA ", "NOVENTA ", "CIEN " };

    private static final String[] CENTENAS = { "CIENTO ", "DOSCIENTOS ", "TRESCIENTOS ", "CUATROCIENTOS ",
            "QUINIENTOS ", "SEISCIENTOS ", "SETECIENTOS ", "OCHOCIENTOS ", "NOVECIENTOS " };

    @Override
    public String obtenerImporteLetra(BigDecimal total, CMoneda moneda) {
        switch (moneda.value()) {
        case "MXN":
            return depuraEspacios(String.format("%s %s", convertNumberToLetter(total.toString(), "PESOS"), "M.N."));
        case "USD":
            return depuraEspacios(String.format("%s %s", convertNumberToLetter(total.toString(), "DOLARES"), "USD"));
        default:
            throw new RuntimeException("Opción de conversion para la moneda no implementada.");
        }
    }

    private String depuraEspacios(String value) {
        return value.replaceAll("\\s\\s", " ");
    }

    public static String convertNumberToLetter(String number, String moneda) throws NumberFormatException {
        String converted = new String();

        DecimalFormat decimalFormat = new DecimalFormat("########0.00",
                new DecimalFormatSymbols(new Locale("es", "MX")));
        number = decimalFormat.format(Double.valueOf(number));
        String splitNumber[] = number.replace('.', '#').split("#");

        // Descompone el trio de millones - ¡SGT!
        int millon = Integer.parseInt(String.valueOf(getDigitAt(splitNumber[0], 8))
                + String.valueOf(getDigitAt(splitNumber[0], 7)) + String.valueOf(getDigitAt(splitNumber[0], 6)));
        if (millon == 1)
            converted = "UN MILLON ";
        if (millon > 1)
            converted = convertNumber(String.valueOf(millon)) + "MILLONES ";

        // Descompone el trio de miles - ¡SGT!
        int miles = Integer.parseInt(String.valueOf(getDigitAt(splitNumber[0], 5))
                + String.valueOf(getDigitAt(splitNumber[0], 4)) + String.valueOf(getDigitAt(splitNumber[0], 3)));
        if (miles == 1)
            converted += "MIL ";
        if (miles > 1)
            converted += convertNumber(String.valueOf(miles)) + "MIL ";

        // Descompone el ultimo trio de unidades - ¡SGT!
        int cientos = Integer.parseInt(String.valueOf(getDigitAt(splitNumber[0], 2))
                + String.valueOf(getDigitAt(splitNumber[0], 1)) + String.valueOf(getDigitAt(splitNumber[0], 0)));
        if (cientos == 1)
            converted += "UN";
        if (millon + miles + cientos == 0)
            converted += "CERO";
        if (cientos > 1)
            converted += convertNumber(String.valueOf(cientos));
        converted += " " + moneda;

        // Descompone los centavos - Camilo
        int centavos = Integer.parseInt(String.valueOf(getDigitAt(splitNumber[1], 2))
                + String.valueOf(getDigitAt(splitNumber[1], 1)) + String.valueOf(getDigitAt(splitNumber[1], 0)));
        if (centavos > 0)
            // converted += " " + String.valueOf(centavos);
            converted += " " + obtenerformatoDecimal(centavos);
        if (centavos <= 0)
            converted += " 00";
        return (converted + "/100").toUpperCase();
    }

    /**
     * Convierte los trios de numeros que componen las unidades, las decenas y
     * las centenas del numero.
     * <p>
     * Creation date 3/05/2006 - 05:33:40 PM
     *
     * @param number
     *            Numero a convetir en digitos
     * @return Numero convertido en letras
     * @since 1.0
     */
    private static String convertNumber(String number) {
        if (number.length() > 3)
            throw new NumberFormatException("La longitud maxima debe ser 3 digitos");
        String output = new String();
        if (getDigitAt(number, 2) != 0)
            output = CENTENAS[getDigitAt(number, 2) - 1];
        int k = Integer.parseInt(String.valueOf(getDigitAt(number, 1)) + String.valueOf(getDigitAt(number, 0)));
        if (k <= 20)
            output += UNIDADES[k];
        else {
            if (k > 30 && getDigitAt(number, 0) != 0)
                output += DECENAS[getDigitAt(number, 1) - 2] + "Y " + UNIDADES[getDigitAt(number, 0)];
            else
                output += DECENAS[getDigitAt(number, 1) - 2] + UNIDADES[getDigitAt(number, 0)];
        }

        // Caso especial con el 100
        if (getDigitAt(number, 2) == 1 && k == 0)
            output = "CIEN";
        return output;
    }

    /**
     * Retorna el digito numerico en la posicion indicada de derecha a izquierda
     * <p>
     * Creation date 3/05/2006 - 05:26:03 PM
     *
     * @param origin
     *            Cadena en la cual se busca el digito
     * @param position
     *            Posicion de derecha a izquierda a retornar
     * @return Digito ubicado en la posicion indicada
     * @since 1.0
     */
    private static int getDigitAt(String origin, int position) {
        if (origin.length() > position && position >= 0)
            return origin.charAt(origin.length() - position - 1) - 48;
        return 0;
    }

    public static String obtenerformatoDecimal(int Cantidad) {
        DecimalFormat decimalFormat = new DecimalFormat("00");
        try {
            return decimalFormat.format(Cantidad);
        } catch (Exception e) {
            e.printStackTrace();

            return "00";
        }
    }

}
