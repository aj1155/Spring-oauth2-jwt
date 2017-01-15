package com.example.service;

import com.example.domain.JWTAuthentication;
import com.example.exception.JWTAuthenticationException;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Manki Kim on 2017-01-15.
 */
@Service
public class JWTAuthenticationService {

    private final String authorizationSchema = "bearer";
    private final int authorizationSchemaLength = 6;

    @Value("${jwt.authentication.secret}")
    private String secret = "";

    @Value("${jwt.authentication.issuer}")
    private String issuer = "";

    @Value("${jwt.authentication.audience}")
    private String audience = "";

    private JwtConsumer jwtConsumer = null;

    private JwtConsumer getJwtConsumer(){
        if (jwtConsumer != null) {
            return jwtConsumer;
        }

        jwtConsumer = new JwtConsumerBuilder()
                .setRequireExpirationTime()
                .setAllowedClockSkewInSeconds(30)
                .setRequireSubject()
                .setExpectedIssuer(issuer)
                .setExpectedAudience(audience)
                .setJwsAlgorithmConstraints(AlgorithmConstraints.DISALLOW_NONE)
                .setVerificationKey(new HmacKey(secret.getBytes()))
                .build();

        return jwtConsumer;
    }

    public Authentication getAuthenticationFromBearer(HttpServletRequest request){

        String stringToken = request.getHeader("Authorization");
        if (stringToken == null) {
            System.out.println("1");
            return null;
        }

        if (stringToken.indexOf(authorizationSchema) == -1) {
            System.out.println("2");
            return null;
        }
        stringToken = stringToken.substring(authorizationSchemaLength).trim();

        try
        {
            JwtClaims jwtClaims = getJwtConsumer().processToClaims(stringToken);
            System.out.println(jwtClaims.toJson());
            return new JWTAuthentication(jwtClaims);
        }
        catch (InvalidJwtException e)
        {
            System.out.println(e);
            throw new JWTAuthenticationException();
        }
    }
}
