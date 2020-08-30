package com.matheuscordeiro.myfinancesapi.exceptions;

public class AuthenticationError extends RuntimeException{
    public AuthenticationError(String e) {
        super(e);
    }
}
