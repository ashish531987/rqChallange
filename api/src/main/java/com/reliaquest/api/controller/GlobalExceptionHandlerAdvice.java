package com.reliaquest.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandlerAdvice {
    @ExceptionHandler(EmployeeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String employeeNotFoundHandler(EmployeeNotFoundException ex) {
        log.error("Error handling web request.", ex);
        return ex.getMessage();
    }

    @ExceptionHandler(TooMayRequestsException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    String exceptionHandler(Exception e){
        log.error("Error handling web request.", e);
        return e.getMessage();
    }
}
