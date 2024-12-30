package com.pjsh.ecommerceapp.handlers;

import com.pjsh.ecommerceapp.exceptions.ProductAlreadyExistsException;
import com.pjsh.ecommerceapp.exceptions.ProductDoesNotExistException;
import com.pjsh.ecommerceapp.exceptions.UserAlreadyExistsException;
import com.pjsh.ecommerceapp.exceptions.UserDoesNotExistException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handle(UserAlreadyExistsException exp) {
        return ResponseEntity.status(BAD_REQUEST).body(exp.getMessage());
    }

    @ExceptionHandler(UserDoesNotExistException.class)
    public ResponseEntity<String> handle(UserDoesNotExistException exp) {
        return ResponseEntity.status(BAD_REQUEST).body(exp.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exp) {
        var errors = new HashMap<String, String>();
        exp.getBindingResult().getAllErrors()
                .forEach(error -> {
                    var fieldName = ((FieldError) error).getField();
                    var errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });

        return ResponseEntity
                .status(BAD_REQUEST)
                .body(new ErrorResponse(errors));
    }

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<String> handle(ProductAlreadyExistsException exp) {
        return ResponseEntity.status(BAD_REQUEST).body(exp.getMessage());
    }

    @ExceptionHandler(ProductDoesNotExistException.class)
    public ResponseEntity<String> handle(ProductDoesNotExistException exp) {
        return ResponseEntity.status(BAD_REQUEST).body(exp.getMessage());
    }

}
