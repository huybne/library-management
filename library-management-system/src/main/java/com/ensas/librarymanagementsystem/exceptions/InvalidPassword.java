package com.ensas.librarymanagementsystem.exceptions;

public class InvalidPassword extends RuntimeException {
    public InvalidPassword(String  message){
        super(message);
    }
}
