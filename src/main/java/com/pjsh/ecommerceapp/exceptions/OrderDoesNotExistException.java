package com.pjsh.ecommerceapp.exceptions;

public class OrderDoesNotExistException extends RuntimeException{

    public OrderDoesNotExistException(String message){
        super(message);
    }

}
