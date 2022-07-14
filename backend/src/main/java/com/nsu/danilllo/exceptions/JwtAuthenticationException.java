package com.nsu.danilllo.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

@Getter
public class JwtAuthenticationException extends AuthenticationException {
    private HttpStatus status;

    public JwtAuthenticationException(String msg, HttpStatus httpStatus) {
        super(msg);
        this.status = httpStatus;
    }

    public JwtAuthenticationException(String msg) {
        super(msg);
    }
}
