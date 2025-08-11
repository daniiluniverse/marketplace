package org.example.marketplace.customerapp.client.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ClientBadRequestException extends RuntimeException {

    private final Map<String, String> errors;

    public ClientBadRequestException(String message, Map<String, String> errors) {
        super(message);
        this.errors = errors;
    }

    }
