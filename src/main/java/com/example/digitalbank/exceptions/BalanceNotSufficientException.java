package com.example.digitalbank.exceptions;

public class BalanceNotSufficientException extends Exception{
    public BalanceNotSufficientException(String message){
        super(message);
    }
}
