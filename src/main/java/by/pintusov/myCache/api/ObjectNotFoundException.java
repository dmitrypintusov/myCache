package by.pintusov.myCache.api;

/**
 *  Object not found exception
 * @author pintusov
 */

public class ObjectNotFoundException extends Exception {

	public ObjectNotFoundException(String message) {
		super(message);
	}

	public ObjectNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
