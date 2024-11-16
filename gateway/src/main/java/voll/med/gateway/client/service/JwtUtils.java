package voll.med.gateway.client.service;

import org.springframework.beans.factory.annotation.Value;

import java.security.Key;
import java.util.Date;

public class JwtUtils {
//    @Value("${jwt.secret}")
//    private String secret;
//
//    private Key key;
//
//    @PostConstruct
//    public void initKey() {
//        this.key = Keys.hmacShaKeyFor(secret.getBytes());
//    }
//
//    public Claims getClaims(String token) {
//        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
//    }
//
//    public boolean isExpired(String token) {
//        try {
//            return getClaims(token).getExpiration().before(new Date());
//        } catch (Exception e) {
//            return false;
//        }
//    }
}
