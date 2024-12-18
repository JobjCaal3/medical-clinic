package voll.med.gateway.Service;

import org.apache.commons.logging.Log;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtUtils {
    @Value("${security.jwt.key.private}")
    private String secretKey;
    @Value("${security.jwt.user.generator}")
    private String propietarieGenerator;

    /**
     * it is resposible for verifying the token is valid, so if the token is not expired
     * and if the token contains de propietarie, signature or token is not altered
     * @param bearerToken contains the token
     * @return
     */
    public Boolean validateToken(String bearerToken) {
        String token = bearerToken.substring(7, bearerToken.length());
            Algorithm algorithm = Algorithm.HMAC256(secretKey);

            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(propietarieGenerator)
                    .build();

            verifier.verify(token);
            return true;
    }
}
