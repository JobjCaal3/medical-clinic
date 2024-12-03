package med.voll.security_service.infra.exceptions;

import feign.FeignException;
import med.voll.security_service.domain.client.doctor.model.Specialty;
import med.voll.security_service.domain.user.model.User;
import med.voll.security_service.domain.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class TreatedExeptions {

    @ExceptionHandler(ValidationIntegration.class)
    public ResponseEntity tratarDeserializable400(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<DatosErrorValidacion>> tratarError400(MethodArgumentNotValidException e) {
        List<DatosErrorValidacion> errores = e.getBindingResult().getFieldErrors().stream()
                .map(DatosErrorValidacion::new).collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errores);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<String> handleFeignException(FeignException ex) {
        return ResponseEntity.status(ex.status()).body("cause" + ex.getMessage() + " " + ex.getCause());
    }

    @ExceptionHandler(FeignException.ServiceUnavailable.class)
    public ResponseEntity<String> handleFeignExceptionServiceUnavailable(FeignException.ServiceUnavailable ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Service is unavailable: " + ex.contentUTF8());
    }

    @ExceptionHandler(FeignException.BadRequest.class)
    public ResponseEntity<?> handleFeignExceptionBadRequest(FeignException.BadRequest e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.contentUTF8());
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> handleDateTimeParseException(DateTimeParseException e) {
        return ResponseEntity.badRequest()
                .body("Invalid date format. Expected format is 'yyyy-MM-dd'. Please check your input: " + e.getParsedString());
    }

    private record DatosErrorValidacion(String campo, String error) {
        public DatosErrorValidacion(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }
}
