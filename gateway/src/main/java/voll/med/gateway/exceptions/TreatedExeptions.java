package voll.med.gateway.exceptions;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TreatedExeptions {

    @ExceptionHandler(ValidationIntegration.class)
    public ResponseEntity<String> treatedValidationItegration(ValidationIntegration e){
        return ResponseEntity.status(e.getStatus()).body(e.getMessage());
    }


    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<String> treatedTokenExpired(TokenExpiredException e){
        String message = "token Expired";
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<String> treatedTokenVerification(JWTVerificationException e){
        String message = "token invalid";
        return ResponseEntity.badRequest().body(message);
    }
}
