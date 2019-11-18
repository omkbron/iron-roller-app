package mx.com.ironroller.model;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DatosAddenda {
    @NotEmpty(message = "El número de autorización es requerido")
    private String numeroAutorizacion;
    @NotEmpty(message = "El contacto de compras es requerido")
    private String contactoCompras;
}
