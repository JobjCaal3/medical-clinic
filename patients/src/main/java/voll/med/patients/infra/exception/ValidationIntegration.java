package voll.med.patients.infra.exception;

import org.springframework.http.HttpStatus;

public class ValidationIntegration extends RuntimeException{

    public ValidationIntegration(String s) {
        super(s);
    }

}
