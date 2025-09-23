package dev.apookash55.finance.exception;

import dev.apookash55.finance.dto.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(Exception ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(ex.getMessage(), request.getRequestURI(), LocalDateTime.now());
        return ResponseEntity.badRequest().body(apiError);
    }
}
