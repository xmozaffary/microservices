package se.moln.productservice.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.net.BindException;
import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleValidation_WithMethodArgumentNotValidException_ShouldReturnBadRequest() {
        // Given
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getMessage()).thenReturn("Validation failed: name is required");

        // When
        ResponseEntity<?> response = exceptionHandler.handleValidation(exception);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.get("error")).isEqualTo("validation_failed");
        assertThat(body.get("message")).isEqualTo("Validation failed: name is required");
    }

    @Test
    void handleValidation_WithBindException_ShouldReturnBadRequest() {
        // Given
        BindException exception = mock(BindException.class);
        when(exception.getMessage()).thenReturn("Binding failed");

        // When
        ResponseEntity<?> response = exceptionHandler.handleValidation(exception);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.get("error")).isEqualTo("validation_failed");
        assertThat(body.get("message")).isEqualTo("Binding failed");
    }


    @Test
    void handleValidation_WithEmptyMessage_ShouldReturnBadRequest() {
        // Given
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getMessage()).thenReturn("");

        // When
        ResponseEntity<?> response = exceptionHandler.handleValidation(exception);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.get("error")).isEqualTo("validation_failed");
        assertThat(body.get("message")).isEqualTo("");
    }

    @Test
    void handleIllegalState_ShouldReturnConflictResponse() {
        // Given
        IllegalStateException exception = new IllegalStateException("Insufficient stock");

        // When
        ResponseEntity<?> response = exceptionHandler.handleIllegalState(exception);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.get("error")).isEqualTo("conflict");
        assertThat(body.get("message")).isEqualTo("Insufficient stock");
    }


    @Test
    void handleIllegalState_WithEmptyMessage_ShouldReturnConflictResponse() {
        // Given
        IllegalStateException exception = new IllegalStateException("");

        // When
        ResponseEntity<?> response = exceptionHandler.handleIllegalState(exception);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.get("error")).isEqualTo("conflict");
        assertThat(body.get("message")).isEqualTo("");
    }

    @Test
    void handleValidation_ShouldHandleBothExceptionTypes() {
        // Test that the handler can process both types of exceptions in the same method

        // Test with MethodArgumentNotValidException
        MethodArgumentNotValidException methodException = mock(MethodArgumentNotValidException.class);
        when(methodException.getMessage()).thenReturn("Method validation failed");

        ResponseEntity<?> methodResponse = exceptionHandler.handleValidation(methodException);
        assertThat(methodResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        // Test with BindException
        BindException bindException = mock(BindException.class);
        when(bindException.getMessage()).thenReturn("Bind validation failed");

        ResponseEntity<?> bindResponse = exceptionHandler.handleValidation(bindException);
        assertThat(bindResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}