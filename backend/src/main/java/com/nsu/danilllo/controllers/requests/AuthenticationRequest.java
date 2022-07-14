package com.nsu.danilllo.controllers.requests;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String username;
    private String password;
}
