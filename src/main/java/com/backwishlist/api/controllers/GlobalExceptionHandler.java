package com.backwishlist.api.controllers;

import com.backwishlist.api.dtos.response.ErrorResponse;
import com.backwishlist.domain.exceptions.ProductAlreadyExistsException;
import com.backwishlist.domain.exceptions.ProductNotFoundException;
import com.backwishlist.domain.exceptions.WishlistLimitExceededException;
import com.backwishlist.infrastructure.utils.MessageResolver;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageResolver messages;

    public GlobalExceptionHandler(MessageResolver messages) {
        this.messages = messages;
    }

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleProductExists(ProductAlreadyExistsException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler(WishlistLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handleLimitExceeded(WishlistLimitExceededException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFound(ProductNotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(RuntimeException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String rawMessage, HttpServletRequest request) {
        String[] parts = rawMessage.split("#", 2);
        String messageKey = parts[0];
        Object[] args = parts.length > 1 ? new Object[]{ parts[1] } : null;

        String resolvedMessage = messages.get(messageKey, args);
        ErrorResponse body = ErrorResponse.of(status, resolvedMessage, request.getRequestURI());
        return ResponseEntity.status(status).body(body);
    }
}
