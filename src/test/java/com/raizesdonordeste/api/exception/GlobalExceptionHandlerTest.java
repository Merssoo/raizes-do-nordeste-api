package com.raizesdonordeste.api.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
        webRequest = mock(WebRequest.class);
        when(webRequest.getDescription(false)).thenReturn("uri=/test");
    }

    @Test
    void handleBusinessExceptionTest() {
        BusinessException ex = new BusinessException("Business error");
        ResponseEntity<Object> response = exceptionHandler.handleBusinessException(ex, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Business error", body.get("message"));
    }

    @Test
    void handleGenericExceptionTest() {
        Exception ex = new Exception("Generic error");
        ResponseEntity<Object> response = exceptionHandler.handleGenericException(ex, webRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Ocorreu um erro interno no servidor", body.get("message"));
    }
}
