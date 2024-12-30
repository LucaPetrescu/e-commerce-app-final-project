package com.pjsh.ecommerceapp.exceptions;

public class ProductDoesNotExistException extends RuntimeException{

    public ProductDoesNotExistException(String message){
        super(message);
    }

}
