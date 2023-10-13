package com.mdn.backend.exception.handle;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public static final String ERROR = "error";
    public static final String MESSAGE = "message";

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleInvalidArgument(MethodArgumentNotValidException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put(ERROR, "Invalid Argument");
        errorMap.put(MESSAGE, ex.getMessage());
        return errorMap;
    }

    @ExceptionHandler(SignatureException.class)
    public Map<String, String> handleSignatureException(SignatureException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put(ERROR, "Invalid Token");
        errorMap.put(MESSAGE, ex.getMessage());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public Map<String, String> handleAccessDeniedException(AccessDeniedException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put(ERROR, "Access Denied");
        errorMap.put(MESSAGE, ex.getMessage());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(InsufficientAuthenticationException.class)
    public Map<String, String> handleInsufficientAuthentication(InsufficientAuthenticationException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put(ERROR, "Insufficient Authentication");
        errorMap.put(MESSAGE, ex.getMessage());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public Map<String, String> handleAuthenticationException(AuthenticationException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put(ERROR, "Authentication Failure");
        errorMap.put(MESSAGE, ex.getMessage());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public Map<String, String> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put(ERROR, "Resource Not Found");
        errorMap.put(MESSAGE, ex.getMessage());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public Map<String, String> handleBusinessException(RuntimeException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put(ERROR, "Business Exception");
        errorMap.put(MESSAGE, ex.getMessage());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Map<String, String> handleGenericException(Exception ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put(ERROR, "Internal Server Error");
        errorMap.put(MESSAGE, ex.getMessage());
        return errorMap;
    }

}
