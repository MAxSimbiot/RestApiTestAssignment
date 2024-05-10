package com.test.assignment.resource;

import com.test.assignment.exception.EntityNotFoundException;
import com.test.assignment.exception.InvalidPathVariableException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Order(-1)
@ControllerAdvice
public class ErrorHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handle(Exception ex) {
    HttpStatus status =
        ex instanceof ConstraintViolationException || ex instanceof InvalidPathVariableException
            ? HttpStatus.BAD_REQUEST
            : ex instanceof EntityNotFoundException
                ? HttpStatus.NOT_FOUND
                : HttpStatus.INTERNAL_SERVER_ERROR;

    ErrorResponse response = ErrorResponse.create(ex, status, "error");
    return new ResponseEntity<>(response, status);
  }
}
