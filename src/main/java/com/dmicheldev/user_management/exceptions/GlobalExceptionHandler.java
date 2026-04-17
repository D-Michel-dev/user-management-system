package com.dmicheldev.user_management.exceptions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.dmicheldev.user_management.user.exceptions.EmailAlreadyExistsException;
import com.dmicheldev.user_management.user.exceptions.ForbiddenException;
import com.dmicheldev.user_management.user.exceptions.InvalidCredentialsException;
import com.dmicheldev.user_management.user.exceptions.UserNotFoundException;

import jakarta.servlet.http.HttpServletRequest;


@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{

    private static final String UNEXPECTED_ERROR_MESSAGE = "An unexpected error occurred. Try again later";


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request){

        Map<String, List<String>> errors = new HashMap<>();

        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()){
            String fieldName = fieldError.getField();
            String message = fieldError.getDefaultMessage();

            if(!errors.containsKey(fieldName)){
                errors.put(fieldName, new ArrayList<>());
            }
            errors.get(fieldName).add(message);
        }
        
    ValidationErrorResponse errorResponse = new ValidationErrorResponse(
            status.value(),
            "Validation failed.",
            "One or more fields are invalid.",
            ((ServletWebRequest) request).getRequest().getRequestURI(),
            LocalDateTime.now(),
            errors
    );
        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(EmailAlreadyExistsException exception, HttpServletRequest request){
        return buildError(HttpStatus.CONFLICT, exception.getMessage(), request);
    }
    
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(ForbiddenException exception, HttpServletRequest request){
        return buildError(HttpStatus.FORBIDDEN, exception.getMessage(), request);
    }
    
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredential(InvalidCredentialsException exception, HttpServletRequest request){
        return buildError(HttpStatus.BAD_REQUEST, exception.getMessage(), request);
    }
    
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException exception, HttpServletRequest request) {
        return buildError(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception, HttpServletRequest request) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, UNEXPECTED_ERROR_MESSAGE, request);
    }

    //=======================================
    //============HELPER METHODS=============
    //=======================================

    private ResponseEntity<ErrorResponse> buildError(
        HttpStatus status, 
        String message, 
        HttpServletRequest request
    ) { 
        ErrorResponse errorResponse = new ErrorResponse(
                status.value(), 
                status.getReasonPhrase(), 
                message, 
                request.getRequestURI(), 
                LocalDateTime.now()
            );
            return ResponseEntity.status(status).body(errorResponse);
    }
}