package com.stargazerstudios.existence.conductor.erratum;

import com.stargazerstudios.existence.conductor.erratum.authorization.UserNotLoggedInException;
import com.stargazerstudios.existence.conductor.erratum.root.*;
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

    @ExceptionHandler(AuthorizationErrorException.class)
    protected ResponseEntity<Object> handleAuthenticationError(
            AuthorizationErrorException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getHttpStatus());
        errorResponse.setMessage(ex.getMessage());
        return buildResponseEntity(errorResponse);
    }

    @ExceptionHandler(DatabaseErrorException.class)
    protected ResponseEntity<Object> handleDatabaseError(
            DatabaseErrorException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getHttpStatus());
        errorResponse.setMessage(ex.getMessage());
        return buildResponseEntity(errorResponse);
    }

    @ExceptionHandler(EntityErrorException.class)
    protected ResponseEntity<Object> handleEntityError(
            EntityErrorException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getHttpStatus());
        errorResponse.setMessage(ex.getMessage());
        return buildResponseEntity(errorResponse);
    }

    @ExceptionHandler(SystemErrorException.class)
    protected ResponseEntity<Object> handleSystemError(
            SystemErrorException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getHttpStatus());
        errorResponse.setMessage(ex.getMessage());
        return buildResponseEntity(errorResponse);
    }

    @ExceptionHandler(ThirdPartyErrorException.class)
    protected ResponseEntity<Object> handleThirdPartyError(
            ThirdPartyErrorException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getHttpStatus());
        errorResponse.setMessage(ex.getMessage());
        return buildResponseEntity(errorResponse);
    }

    @ExceptionHandler(UnknownInputException.class)
    protected ResponseEntity<Object> handleUnknownInputError(
            UnknownInputException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getHttpStatus());
        errorResponse.setMessage(ex.getMessage());
        return buildResponseEntity(errorResponse);
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<Object> handleCoreAuthenticationError(
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
