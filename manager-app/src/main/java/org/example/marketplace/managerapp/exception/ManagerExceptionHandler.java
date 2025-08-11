package org.example.marketplace.managerapp.exception;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@RequiredArgsConstructor
public class ManagerExceptionHandler {

    private  final Logger log = LoggerFactory.getLogger(ManagerExceptionHandler.class);

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Map<String, Object>> handleCatalogueServiceError(HttpClientErrorException e){

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "error");
        response.put("code", "REMOTE_SERVICE_ERROR");
        response.put("message", e.getStatusText());
        response.put("details", e.getResponseBodyAsString());

        log.error("Catalogue service error: {} - {}", e.getStatusCode(), e.getStatusText());

        return ResponseEntity.status(e.getStatusCode()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException e){

        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> errors = bindingResult.getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Invalid field"
                ));

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("status", "error");
        response.put("code", "VALIDATION_FAILED");
        response.put("errors", errors);

        log.warn("Validation error: {}", errors);

        return ResponseEntity.badRequest().body(response);
    }


}
