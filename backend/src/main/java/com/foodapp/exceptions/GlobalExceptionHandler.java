package com.foodapp.exceptions;

import com.foodapp.constants.ErrorCode;
import com.foodapp.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse<?>> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse<?> apiResponse = new ApiResponse<>();

        apiResponse.setErrorCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        apiResponse.setStatus(ApiResponse.Status.ERROR);
        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        ApiResponse<?> apiResponse = new ApiResponse<>();
        apiResponse.setErrors(errors);
        apiResponse.setStatus(ApiResponse.Status.ERROR);
        apiResponse.setMessage("Validation Failed");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }
}
