package pl.exadel.milavitsky.tenderflex.exception;

/**
 * Exceptions of controller, if body or value of parameters not valid return this object
 */

public class ControllerException extends Exception {

    public ControllerException() {
        super();
    }

    public ControllerException(String message) {
        super(message);
    }

    public ControllerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ControllerException(Throwable cause) {
        super(cause);
    }
}