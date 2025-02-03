package com.rodrigo.starwars.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GeneralExceptionHandler {
  @ExceptionHandler(MethodArgumentNotValidException.class)
  private ResponseEntity<Object> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
    List<String> errors = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .collect(Collectors.toList());

    Map<String, Object> response = new LinkedHashMap<>();
    response.put("timestamp", LocalDateTime.now());
    response.put("status", HttpStatus.UNPROCESSABLE_ENTITY.value());
    response.put("error", "Unprocessable Entity");
    response.put("message", "Validation failed");
    response.put("path", request.getRequestURI());
    response.put("details", errors);

    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response); //422 Unprocessable Entity
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  private ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT) //409 Confict
    .body(ex.getMessage());
  }

  @ExceptionHandler(EmptyResultDataAccessException.class)
  private ResponseEntity<Object> handleBadRequest(EmptyResultDataAccessException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND) //404 Not Found
    .body(ex.getMessage());
  }

}

