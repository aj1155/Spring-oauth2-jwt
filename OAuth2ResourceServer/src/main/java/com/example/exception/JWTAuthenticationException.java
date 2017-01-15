package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.security.core.AuthenticationException;
/**
 * Created by Manki Kim on 2017-01-15.
 */
@ResponseStatus(value= HttpStatus.FORBIDDEN) // Status Code 403
public class JWTAuthenticationException extends AuthenticationException {

    private static final long serialVersionUID = -551156175648379326L;

    public JWTAuthenticationException() {
        super("Invalid JWT token");
    }

    public JWTAuthenticationException(String message) {
        super(message);
    }
}