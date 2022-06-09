package com.example.digitalbank.exceptions;

public class BankAccountNotFoundException extends Exception{
    public BankAccountNotFoundException(String message){
        super(message);
    }
}
