package com.pjsh.ecommerceapp.exceptions;

public class CategoryAlreadyExistsException extends RuntimeException{

    public CategoryAlreadyExistsException(String message){
        super(message);
    }

}
