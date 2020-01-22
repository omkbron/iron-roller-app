package mx.com.ironroller.model;

public class ConceptoDescripcionException extends RuntimeException {

	public ConceptoDescripcionException() {
	}

	public ConceptoDescripcionException(String message) {
		super(message);
	}

	public ConceptoDescripcionException(Throwable cause) {
		super(cause);
	}

	public ConceptoDescripcionException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConceptoDescripcionException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
