package io.github.pauliustechin.egzaminas.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

    private final int status;
    private final String error;
    private final String message;
    private final List<MyFieldError> fieldErrors;

    public ApiError(int status, String error, String message, List<MyFieldError> fieldErrors) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.fieldErrors = fieldErrors;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public List<MyFieldError> getFieldErrors() {
        return fieldErrors;
    }
}