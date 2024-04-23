package com.library.bookrental.exceptions;

public class RentalNotFoundException extends RuntimeException{
    public RentalNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public RentalNotFoundException(String message){
        super(message);
    }
}
