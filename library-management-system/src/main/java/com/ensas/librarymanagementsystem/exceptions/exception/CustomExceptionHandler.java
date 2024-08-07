package com.ensas.librarymanagementsystem.exceptions.exception;

import com.ensas.librarymanagementsystem.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleBookNotFoundException(BookNotFoundException e){
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                e.getMessage(), new Date(), badRequest
        );
        return new ResponseEntity<>(exceptionResponse, badRequest);
    }

    @ExceptionHandler(AuthorNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleAuthorNotFoundException(AuthorNotFoundException e){
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                e.getMessage(), new Date(), badRequest
        );
        return new ResponseEntity<>(exceptionResponse, badRequest);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleCategoryNotFoundException(CategoryNotFoundException e){
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                e.getMessage(), new Date(), badRequest
        );
        return new ResponseEntity<>(exceptionResponse, badRequest);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationExceptions(MethodArgumentNotValidException ex){
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                ex.getMessage(), new Date(), badRequest
        );
        return new ResponseEntity<>(exceptionResponse, badRequest);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundException(NotFoundException e){
        HttpStatus notFound = HttpStatus.NOT_FOUND;
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                e.getMessage(), new Date(), notFound
        );
        return new ResponseEntity<>(exceptionResponse, notFound);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUsernameNotFoundException(UsernameNotFoundException e){
        HttpStatus notFound = HttpStatus.NOT_FOUND;
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                e.getMessage(), new Date(), notFound
        );
        return new ResponseEntity<>(exceptionResponse, notFound);
    }

    @ExceptionHandler(InvalidPassword.class)
    public ResponseEntity<ExceptionResponse> handleInvalidPasswordException(InvalidPassword e){
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                e.getMessage(), new Date(), badRequest
        );
        return new ResponseEntity<>(exceptionResponse, badRequest);
    }
}
