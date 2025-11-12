package com.example.matchservice.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 when repository finds nothing
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(EmptyResultDataAccessException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("status", "error", "message", "Resource not found"));
    }

    // 400 for validation errors (@Valid annotations)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException e) {
        // Collect all field errors into a single message
        String message = e.getBindingResult().getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("status", "error", "message", message));
    }

    // 400 for JSON deserialization issues or invalid format
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        String message = "Malformed JSON or invalid field value";

        if (e.getCause() instanceof InvalidFormatException ife) {
            String field = ife.getPath().isEmpty() ? "unknown" : ife.getPath().get(0).getFieldName();
            String targetType = ife.getTargetType() != null ? ife.getTargetType().getSimpleName() : "value";
            message = "Invalid value for field '" + field + "'. Expected type: " + targetType;
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("status", "error", "message", message));
    }

    // 400 for runtime parsing errors in service (dates, numbers, enums)
    @ExceptionHandler({DateTimeParseException.class, NumberFormatException.class, IllegalArgumentException.class})
    public ResponseEntity<Map<String, String>> handleBadRequest(Exception e) {
        String message;
        if (e instanceof DateTimeParseException) {
            message = "Invalid date or time format";
        } else if (e instanceof IllegalArgumentException) {
            message = e.getMessage();
        } else {
            message = "Invalid request";
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("status", "error", "message", message));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        String rootMessage = e.getMostSpecificCause().getMessage().toLowerCase();
        String message;

        if (rootMessage.contains("match_id_specifier")) {
            message = "Duplicate odd for this match and specifier";
        } else if (rootMessage.contains("match_id")) {
            message = "Invalid match_id — match does not exist";
        } else {
            message = "Database constraint violation";
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("status", "error", "message", message));
    }

    // Catch-all for unexpected server errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleAll(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("status", "error", "message", "Something went wrong"));
    }
}
