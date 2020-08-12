package com.virtualtryon.backend.payload;

public class LoginRequest {
    private String usernameOrEmail;

    private String password;

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public String getPassword() {
        return password;
    }
}