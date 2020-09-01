package com.matheuscordeiro.myfinancesapi.exceptions;

public class AuthenticationErrorException extends RuntimeException{
    public AuthenticationErrorException(String e) {
        super(e);
    }
}
