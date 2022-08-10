package com.geneyp.exception;


public class ApiException extends RuntimeException {
    private static final long serialVersionUID = -6893448453031353171L;

    public ApiException() {
        super();
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiException(String message) {
        super(message);
    }

    public ApiException(Throwable cause) {
        super(cause);
    }
}
