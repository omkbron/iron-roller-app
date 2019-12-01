package mx.com.ironroller.model;

public class ComprobanteInvalidoException extends RuntimeException {

    public ComprobanteInvalidoException() {
        // TODO Auto-generated constructor stub
    }

    public ComprobanteInvalidoException(String message) {
        super(message);
    }

    public ComprobanteInvalidoException(Throwable cause) {
        super(cause);
    }

    public ComprobanteInvalidoException(String message, Throwable cause) {
        super(message, cause);
    }

    public ComprobanteInvalidoException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
