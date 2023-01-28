package pl.exadel.milavitsky.tenderflex.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.concurrent.atomic.AtomicInteger;

@RestControllerAdvice
public class RestExceptionHandler {
    private static final HttpStatus badRequest = HttpStatus.BAD_REQUEST;
    private static final HttpStatus notFound = HttpStatus.NOT_FOUND;

    private final AtomicInteger atomicInteger = new AtomicInteger();

    /**
     * If api throw service exception we create our custom exception and code auto increment for all users
     *
     * @param exception from service
     * @return our custom exception
     */
    @ExceptionHandler(ServiceException.class)
    private ResponseEntity<ErrorResponse> handlerServiceException(ServiceException exception) {
        ErrorResponse errorResponse = errorResponseHandler(exception, notFound);
        return ResponseEntity.status(notFound).body(errorResponse);
    }

    /**
     * If we had ControllerException we catch then return our exception
     *
     * @param exception runtime from api that illegal validation
     * @return custom exception
     */

    @ExceptionHandler(ControllerException.class)
    private ResponseEntity<ErrorResponse> handlerControllerException(ControllerException exception) {
        ErrorResponse errorResponse = errorResponseHandler(exception, notFound);
        return ResponseEntity.status(badRequest).body(errorResponse);
    }

    /**
     * If we had IllegalArgumentException or IllegalStateException we catch then return our exception
     *
     * @param exception runtime from api
     * @return custom exception
     */
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    private ResponseEntity<ErrorResponse> handlerRuntimeException(RuntimeException exception) {
        String errormessage = "Illegal argument exception. " + exception.getLocalizedMessage();
        Integer errorCode = badRequest.value() * 10 + atomicInteger.getAndIncrement();
        ErrorResponse errorResponse = new ErrorResponse(errormessage, errorCode);
        return ResponseEntity.status(badRequest).body(errorResponse);
    }

    /**
     * If we had IllegalArgumentException or IllegalStateException we catch then return our exception
     *
     * @param exception runtime from api if input parameters bad
     * @return custom exception
     */
    @ExceptionHandler({JsonParseException.class, InvalidFormatException.class})
    private ResponseEntity<ErrorResponse> handlerInputException(RuntimeException exception) {
        String errormessage = "Illegal argument exception. " + exception.getLocalizedMessage();
        Integer errorCode = badRequest.value() * 10 + atomicInteger.getAndIncrement();
        ErrorResponse errorResponse = new ErrorResponse(errormessage, errorCode);
        return ResponseEntity.status(badRequest).body(errorResponse);
    }

    /**
     * @param exception from others exceptions classes
     * @param status is status of response
     * @return Custom entity exception
     */
    private ErrorResponse errorResponseHandler(Exception exception, HttpStatus status) {
        return new ErrorResponse(exception.getLocalizedMessage(), status.value() * 10
                + atomicInteger.getAndIncrement());
    }
}