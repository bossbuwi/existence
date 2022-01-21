package com.stargazerstudios.existence.conductor.erratum;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;

@Getter @Setter
public class ErrorResponse {

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Timestamp timestamp;

    @JsonIgnore
    private HttpStatus httpStatus;

    private int status;
    private String error;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String path;

    private ErrorResponse() {
        timestamp = new Timestamp(System.currentTimeMillis());
    }

    public ErrorResponse(HttpStatus httpStatus) {
        this();
        this.httpStatus = httpStatus;
        this.status = httpStatus.value();
        this.error = httpStatus.getReasonPhrase();
    }

    public ErrorResponse(HttpStatus httpStatus, Throwable ex) {
        this();
        this.httpStatus = httpStatus;
        this.status = httpStatus.value();
        this.error = httpStatus.getReasonPhrase();
        this.message = "Unexpected error";
    }

    public ErrorResponse(HttpStatus httpStatus, String message, Throwable ex) {
        this();
        this.httpStatus = httpStatus;
        this.status = httpStatus.value();
        this.error = httpStatus.getReasonPhrase();
        this.message = message;
    }
}
