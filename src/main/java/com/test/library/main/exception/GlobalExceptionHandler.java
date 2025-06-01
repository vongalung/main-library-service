package com.test.library.main.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

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

    ResponseEntity<Map<String, Object>> handleBaseApplicationException(HttpServletRequest req,
                                                                       BaseApplicationException ex,
                                                                       HttpStatus status) {
        String message = generateExceptionMessage(ex);
        log.error(message, ex);
        LinkedHashMap<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("timestamp", ZonedDateTime.now());
        responseBody.put("status", status.value());
        responseBody.put("error", message);
        responseBody.put("path", req.getServletPath());
        return new ResponseEntity<>(responseBody, status);
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
