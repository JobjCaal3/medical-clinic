package voll.med.gateway.exceptions;

import org.springframework.http.HttpStatus;

public class ValidationIntegration extends RuntimeException{
    private final HttpStatus status;

    public ValidationIntegration(HttpStatus status, String s) {
        super(s);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
