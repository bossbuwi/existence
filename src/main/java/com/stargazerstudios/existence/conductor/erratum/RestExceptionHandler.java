package com.stargazerstudios.existence.conductor.erratum;

import com.stargazerstudios.existence.conductor.erratum.universal.*;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(
            UserNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getHttpStatus());
        errorResponse.setMessage(ex.getMessage());
        return buildResponseEntity(errorResponse);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(
            EntityNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getHttpStatus());
        errorResponse.setMessage(ex.getMessage());
        return buildResponseEntity(errorResponse);
    }

    @ExceptionHandler(BadGatewayException.class)
    protected ResponseEntity<Object> handleGatewayErrors(
            BadGatewayException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getHttpStatus());
        errorResponse.setMessage(ex.getMessage());
        return buildResponseEntity(errorResponse);
    }

    @ExceptionHandler(GatewayTimeoutException.class)
    protected ResponseEntity<Object> handleGatewayErrors(
            GatewayTimeoutException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getHttpStatus());
        errorResponse.setMessage(ex.getMessage());
        return buildResponseEntity(errorResponse);
    }

    @ExceptionHandler(BadJsonWebTokenException.class)
    protected ResponseEntity<Object> handleJwtErrors(
            BadJsonWebTokenException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getHttpStatus());
        errorResponse.setMessage(ex.getMessage());
        return buildResponseEntity(errorResponse);
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<Object> handleUnauthorizedErrors(
            AuthenticationException ex) {
        UserNotLoggedInException _ex = new UserNotLoggedInException();
        ErrorResponse errorResponse = new ErrorResponse(_ex.getHttpStatus());
        errorResponse.setMessage(_ex.getMessage());
        return buildResponseEntity(errorResponse);
    }

    private ResponseEntity<Object> buildResponseEntity(ErrorResponse errorResponse) {
        return new ResponseEntity<>(errorResponse, errorResponse.getHttpStatus());
    }
}
