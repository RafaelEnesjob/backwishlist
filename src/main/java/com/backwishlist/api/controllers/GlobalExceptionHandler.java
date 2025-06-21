package com.backwishlist.api.controllers;

import com.backwishlist.api.dtos.response.ErrorResponse;
import com.backwishlist.domain.exceptions.ProductAlreadyExistsException;
import com.backwishlist.domain.exceptions.ProductNotFoundException;
import com.backwishlist.domain.exceptions.WishlistLimitExceededException;
import com.backwishlist.domain.exceptions.WishlistNotFoundException;
import com.backwishlist.infrastructure.utils.MessageResolver;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{


    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleProductExists(final ProductAlreadyExistsException ex, final HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler(WishlistLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handleLimitExceeded(final WishlistLimitExceededException ex, final HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFound(final ProductNotFoundException ex, final HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(WishlistNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleWishlistNotFound(final WishlistNotFoundException ex, final HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(final RuntimeException ex, final HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(final HttpStatus status, final String rawMessage, final HttpServletRequest request) {
        final String[] parts = rawMessage.split("#", 2);
        final String messageKey = parts[0];
        Object[] args = parts.length > 1 ? new Object[]{ parts[1] } : null;

        final String resolvedMessage = MessageResolver.get(messageKey, args);
        final ErrorResponse body = ErrorResponse.of(status, resolvedMessage, request.getRequestURI());
        return ResponseEntity.status(status).body(body);
    }
}
