package io.github.pauliustechin.egzaminas.exception;

public class NotAllowedApiActionException extends RuntimeException {
    public NotAllowedApiActionException(String message) {
        super(message);
    }
}
