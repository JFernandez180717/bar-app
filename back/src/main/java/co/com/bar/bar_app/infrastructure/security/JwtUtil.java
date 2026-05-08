package co.com.bar.bar_app.infrastructure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {
    private final String secretKey;
    private final Algorithm algorithm;

    public JwtUtil(@Value("${app.jwt.secret}") String secretKey) {
        this.secretKey = secretKey;
        this.algorithm = Algorithm.HMAC256(secretKey);
    }

    public String create(String username) {
        return JWT.create()
                .withSubject(username)
                .withIssuer("bar-app")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)))
                .sign(this.algorithm);
    }

    public boolean isValid(String jwt) {
        try {
            JWT.require(this.algorithm)
                    .withIssuer("bar-app")
                    .build()
                    .verify(jwt);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public String getUsername(String jwt) {
        return JWT.require(this.algorithm)
                .build()
                .verify(jwt)
                .getSubject();
    }
}
