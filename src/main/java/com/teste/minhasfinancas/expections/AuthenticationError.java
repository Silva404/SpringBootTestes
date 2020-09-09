package com.teste.minhasfinancas.expections;

public class AuthenticationError extends RuntimeException{

    public AuthenticationError(String message) {
        super(message);
    }
}
