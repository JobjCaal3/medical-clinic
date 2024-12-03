package voll.med.doctors.infra.exceptions;

import feign.FeignException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TreatedExeptions {

    @ExceptionHandler(ValidationIntegration.class)
    public ResponseEntity<String> treatedValidationItegration(ValidationIntegration e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        String errorMessage = "The email already exists, try another one";
        return ResponseEntity.badRequest().body(errorMessage);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratedError400(MethodArgumentNotValidException e){
        var errores = e.getFieldErrors().stream().map(DatosErrorValidacion::new).toList();
        return ResponseEntity.badRequest().body(errores);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity tratedFeignExeptionsGeneral(FeignException e){
        return ResponseEntity.status(e.status()).body(e.contentUTF8());
    }

    @ExceptionHandler(FeignException.ServiceUnavailable.class)
    public ResponseEntity TratedFeignExceptionServiceUnavaible(FeignException.ServiceUnavailable e){
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.contentUTF8());
    }

    @ExceptionHandler(FeignException.BadRequest.class)
    public ResponseEntity TratedFeignExceptionBadRequest(FeignException.BadRequest e){
        return ResponseEntity.badRequest().body(e.contentUTF8());
    }

    private record DatosErrorValidacion(String campo, String error){
        public DatosErrorValidacion(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }
}
