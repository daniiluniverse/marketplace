package org.example.marketplace.customerapp.exepton;


import org.example.marketplace.customerapp.client.exception.ClientBadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandler {



    @org.springframework.web.bind.annotation.ExceptionHandler(ClientBadRequestException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleClientBadRequest(
            ClientBadRequestException ex) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("message", ex.getMessage());

        if (!ex.getErrors().isEmpty()) {
            body.put("errors", ex.getErrors());
        }

        return Mono.just(ResponseEntity.badRequest().body(body));
    }


}
