package com.backend.foodproject.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ControllerAdvice
public class GlobalExceptionHandel {

    @ExceptionHandler(CustomExceptionHandling.class)
    public ResponseEntity<ErrorResponse> handelCustomException(CustomExceptionHandling ex){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .status(ex.getStatus())
                .build();

        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handelValidationError(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        for (FieldError error: ex.getFieldErrors()){
            errors.put(error.getField(), error.getDefaultMessage());
        }
        ErrorResponse body = ErrorResponse.builder()
                .message("Validation failed for one or more fields")
                .status(HttpStatus.BAD_REQUEST.value())
                .errors(errors)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(body);
    }
}
