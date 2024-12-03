package med.voll.security_service.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import med.voll.security_service.domain.user.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RefreshScope
public class TokenService {
    @Value("${security.jwt.key.private}")
    private String secretKey;
    @Value("${security.jwt.user.generator}")
    private String propietarieGenerator;

    public String createToken(User user) {
        String JwtToken;
        String userName = user.getUserName();
        //String authorities = user.getRole().stream().map(GrantedAuthority::getAuthority)
        //       .collect(Collectors.joining(","));
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);

            JwtToken = JWT.create()
                    .withIssuer(propietarieGenerator)
                    .withSubject(userName)
                    //.withClaim("authorities", authorities)
                    .withIssuedAt(new Date())
                    .withExpiresAt(generarFechaExpiracion())
                    .withJWTId(UUID.randomUUID().toString())
                    .withNotBefore(new Date(System.currentTimeMillis()))
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new RuntimeException(e.getMessage());
        }
        return JwtToken;
    }

    public Boolean validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);

            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(propietarieGenerator)
                    .build();

            verifier.verify(token);
            return true;
        } catch (TokenExpiredException expiredException) {
            throw new TokenExpiredException("Token expired", expiredException.getExpiredOn());
        }catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Token invalid, not Authorized");
        }
    }

    public String extractUsername(String token) {
        DecodedJWT verifier;
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);

            verifier = JWT.require(algorithm)
                    .withIssuer(propietarieGenerator)
                    .build()
                    .verify(token);

        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Token invalid, not Authorized");
        }
        return verifier.getSubject();
    }

    private Instant generarFechaExpiracion() {
        return Instant.now().plus(Duration.ofHours(3));
    }

    public Claim getSpecificClaim(DecodedJWT decodedJWT, String claimName) {
        return decodedJWT.getClaim(claimName);
    }


}
