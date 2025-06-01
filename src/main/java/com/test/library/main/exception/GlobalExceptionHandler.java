package com.test.library.main.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> badRequest(HttpServletRequest req, BadRequestException ex) {
        return handleBaseApplicationException(req, ex, BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, Object>> unauthorized(HttpServletRequest req, UnauthorizedException ex) {
        return handleBaseApplicationException(req, ex, UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> methodArgumentNotValid(HttpServletRequest req,
                                                                      MethodArgumentNotValidException ex) {
        return handleBaseApplicationException(req, ex, BAD_REQUEST);
    }

    ResponseEntity<Map<String, Object>> handleBaseApplicationException(HttpServletRequest req,
                                                                       Exception ex, HttpStatus status) {
        String message = generateExceptionMessage(ex);
        log.error(message, ex);
        LinkedHashMap<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("timestamp", ZonedDateTime.now());
        responseBody.put("status", status.value());
        responseBody.put("error", message);
        responseBody.put("path", req.getServletPath());
        return new ResponseEntity<>(responseBody, status);
    }

    String generateExceptionMessage(Exception ex) {
        if (ex instanceof BaseApplicationException) {
            return generateExceptionMessage((BaseApplicationException) ex);
        }
        if (ex instanceof MethodArgumentNotValidException) {
            return generateExceptionMessage((MethodArgumentNotValidException) ex);
        }
        String message = ex.getMessage();
        if (message == null || message.isBlank()) {
            return ex.getClass().getSimpleName();
        }
        return message;
    }

    String generateExceptionMessage(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors().stream()
                .map(this::createFieldErrorMessage)
                .collect(Collectors.joining("\n"));
    }

    String createFieldErrorMessage(FieldError e) {
        return e.getField() + " " + e.getDefaultMessage();
    }

    String generateExceptionMessage(BaseApplicationException ex) {
        String exceptionType = ex.getClass().getSimpleName();
        String message = ex.getMessage();
        if (message == null || message.isBlank()) {
            return exceptionType;
        }
        return exceptionType + ": " + message;
    }
}
