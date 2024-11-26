package med.voll.security_service.infra.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TreatedExeptions {
    @ExceptionHandler(ValidationIntegration.class)
    public ResponseEntity tratarDeserializable400(Exception e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
