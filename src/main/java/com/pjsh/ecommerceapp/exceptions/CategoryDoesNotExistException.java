package com.pjsh.ecommerceapp.exceptions;

public class CategoryDoesNotExistException extends RuntimeException {

    public CategoryDoesNotExistException(String message){
        super(message);
    }

}
