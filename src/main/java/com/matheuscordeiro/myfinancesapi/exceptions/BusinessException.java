package com.matheuscordeiro.myfinancesapi.exceptions;

public class BusinessException extends RuntimeException {
    public BusinessException(String e) {
        super(e);
    }
}
