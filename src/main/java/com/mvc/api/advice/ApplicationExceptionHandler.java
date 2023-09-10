package com.mvc.api.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(ApplicationExceptionHandler.class);

    private Map<String, Object> generateErrorResponse(Exception exception) {
        UUID errorUUID = generateUUID();
        Map<String, Object> errorResponse = new HashMap<>();

        // Set the HTTP status and trace
        errorResponse.put("traceId", errorUUID.toString());
        errorResponse.put("requestStatus", HttpStatus.BAD_REQUEST.value());

        // Include the request type (GET, POST, etc.)
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        errorResponse.put("requestType", request.getMethod());

        // Include the endpoint URL
        String endpointUrl = request.getRequestURL().toString();
        errorResponse.put("target", endpointUrl);

        return errorResponse;
    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {
//        UUID errorUUID = generateUUID();
//        Map<String, Object> errorResponse = generateErrorResponse(exception);
//
//        // Include the error message from the exception
//        errorResponse.put("error", "Required request body is missing");
//
//        // Convert the error response to a JSON string
//        String errorJson = convertToJsonString(errorResponse);
//
//        // Write the JSON string to the error.log file
//        writeToJsonLogFile(errorJson);
//
//        return ResponseEntity.badRequest().body(errorResponse);
//    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidArgument(MethodArgumentNotValidException exception) {
        UUID errorUUID = generateUUID();
        Map<String, Object> errorResponse = generateErrorResponse(exception);

        List<Map<String, Object>> errors = new ArrayList<>();

        // Process field errors
        exception.getBindingResult().getFieldErrors().forEach(fieldError -> {
            Map<String, Object> fieldErrorMap = new HashMap<>();
            fieldErrorMap.put("param", fieldError.getField());
            fieldErrorMap.put("error", fieldError.getDefaultMessage());
            fieldErrorMap.put("value", fieldError.getRejectedValue());
            errors.add(fieldErrorMap);
        });


        // Wrap the "errors" list inside a "params" field
        Map<String, Object> params = new HashMap<>();
        params.put("params", errors);

        errorResponse.put("errors", params);

        // Convert the error response to a JSON string
        String errorJson = convertToJsonString(errorResponse);

        // Write the JSON string to the error.log file
        writeToJsonLogFile(errorJson);

        return ResponseEntity.badRequest().body(errorResponse);
    }

    private String convertToJsonString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            // Handle the exception or log it as needed
            e.printStackTrace();
            return "";
        }
    }

    private void writeToJsonLogFile(String json) {
        try (FileWriter fileWriter = new FileWriter("logs/bad_request.log", true)) {
            fileWriter.write(json + "\n"); // Append a newline character
        } catch (IOException e) {
            // Handle the exception or log it as needed
            e.printStackTrace();
        }
    }

    private UUID generateUUID() {
        return UUID.randomUUID();
    }
}