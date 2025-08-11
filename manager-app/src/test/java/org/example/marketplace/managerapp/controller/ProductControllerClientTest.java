package org.example.marketplace.managerapp.controller;

import org.example.marketplace.managerapp.client.ProductRestClient;
import org.example.marketplace.managerapp.dto.RequestProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductControllerClientTest {


    @Mock
    ProductRestClient productRestClient;

    @InjectMocks
    ProductControllerClient controller;

    @Mock
    BindingResult bindingResult;

    @Test
    @DisplayName("CreateProduct создаст новый товар и вернет статус 200")
    void createProduct_RequestIsValid(){

        RequestProduct requestProduct = new RequestProduct("Mac", "MacBook 512GB, BLUE", 250000.0);

        ResponseEntity<?> response = controller.createProduct(requestProduct);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> {
                    Map<?, ?> body = (Map<?, ?>) response.getBody();
                    assertEquals("success", body.get("status"));
                    assertEquals("Product created successfully", body.get("message"));
                    assertEquals(requestProduct, body.get("data"));

                }
        );

        verify(productRestClient).newProduct(requestProduct);
        verifyNoMoreInteractions(productRestClient);
    }







}
