package com.hand.training.controller;

import com.hand.training.model.WebResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<WebResponse<Map<String, String>>> constraintViolationException(ConstraintViolationException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getConstraintViolations().forEach(objectConstraintViolation -> {
            errors.put(objectConstraintViolation.getPropertyPath().toString(), objectConstraintViolation.getMessage());
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(WebResponse.<Map<String, String>>builder().status("error").data(errors).build());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<WebResponse<String>> apiException(ResponseStatusException exception){
        return ResponseEntity.status(exception.getStatusCode())
                .body(WebResponse.<String>builder().status("error").data(exception.getReason()).build());
    }
}
