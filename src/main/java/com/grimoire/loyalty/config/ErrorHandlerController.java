package com.grimoire.loyalty.config;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.grimoire.loyalty.utils.errors.RequestError;
import com.grimoire.loyalty.utils.errors.RequestErrorResponse;
import com.grimoire.loyalty.utils.errors.RequestFieldError;

@RestControllerAdvice
public class ErrorHandlerController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RequestErrorResponse> handleValidationError(MethodArgumentNotValidException exception){
        return Stream
                .concat(
                    exception.getFieldErrors().stream().map(it -> new RequestFieldError(it.getField(), it.getDefaultMessage())),
                    exception.getGlobalErrors().stream().map(it -> new RequestFieldError(it.getCode(), it.getDefaultMessage()))
                )
                .collect(Collectors.collectingAndThen(
                    Collectors.toList(),
                    (errors) -> ResponseEntity.badRequest().body(new RequestErrorResponse("Validation Error", errors))
                ));
    }

    @ExceptionHandler(RequestError.class)
    public ResponseEntity<RequestErrorResponse> handleApiError(RequestError exception){
        return ResponseEntity.status(exception.statusCode()).body(exception.error());
    }
}
