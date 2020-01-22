package mx.com.ironroller.model;

public class IronRollerAppException extends RuntimeException {

	public IronRollerAppException() {
	}

	public IronRollerAppException(String message) {
		super(message);
	}

	public IronRollerAppException(Throwable cause) {
		super(cause);
	}

	public IronRollerAppException(String message, Throwable cause) {
		super(message, cause);
	}

	public IronRollerAppException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
