package se.moln.productservice.exception;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ApiExceptionHandlerTest {

    private ApiExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new ApiExceptionHandler();
    }

    @Test
    void handleDuplicate_ShouldReturnConflictResponse() {
        // Given
        DuplicateProductException exception = new DuplicateProductException("Product already exists");

        // When
        Map<String, Object> response = exceptionHandler.handleDuplicate(exception);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.get("error")).isEqualTo("Conflict");
        assertThat(response.get("message")).isEqualTo("Product already exists");
    }


    @Test
    void handleDuplicate_WithEmptyMessage_ShouldReturnConflictResponse() {
        // Given
        DuplicateProductException exception = new DuplicateProductException("");

        // When
        Map<String, Object> response = exceptionHandler.handleDuplicate(exception);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.get("error")).isEqualTo("Conflict");
        assertThat(response.get("message")).isEqualTo("");
    }

    @Test
    void handleIntegrity_ShouldReturnConflictResponse() {
        // Given
        DataIntegrityViolationException exception = new DataIntegrityViolationException("Unique constraint violation");

        // When
        Map<String, Object> response = exceptionHandler.handleIntegrity(exception);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.get("error")).isEqualTo("Conflict");
        assertThat(response.get("message")).isEqualTo("Unique constraint violated (e.g. slug/name already exists)");
    }

    @Test
    void handleIntegrity_WithNullMessage_ShouldReturnConflictResponse() {
        // Given
        DataIntegrityViolationException exception = new DataIntegrityViolationException(null);

        // When
        Map<String, Object> response = exceptionHandler.handleIntegrity(exception);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.get("error")).isEqualTo("Conflict");
        assertThat(response.get("message")).isEqualTo("Unique constraint violated (e.g. slug/name already exists)");
    }

    @Test
    void handleValidation_ShouldReturnBadRequestWithFieldErrors() {
        // Given
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        
        FieldError fieldError1 = new FieldError("productRequest", "name", "Name is required");
        FieldError fieldError2 = new FieldError("productRequest", "price", "Price must be positive");
        
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(Arrays.asList(fieldError1, fieldError2));

        // When
        Map<String, Object> response = exceptionHandler.handleValidation(exception);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.get("error")).isEqualTo("Validation failed");
        assertThat(response.get("details")).isNotNull();
        @SuppressWarnings("unchecked")
        var details = (java.util.List<Map<String, Object>>) response.get("details");
        assertThat(details).hasSize(2);
        assertThat(details.get(0).get("field")).isEqualTo("name");
        assertThat(details.get(0).get("error")).isEqualTo("Name is required");
        assertThat(details.get(1).get("field")).isEqualTo("price");
        assertThat(details.get(1).get("error")).isEqualTo("Price must be positive");
    }

    @Test
    void handleValidation_WithEmptyFieldErrors_ShouldReturnBadRequest() {
        // Given
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(Arrays.asList());

        // When
        Map<String, Object> response = exceptionHandler.handleValidation(exception);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.get("error")).isEqualTo("Validation failed");
        @SuppressWarnings("unchecked")
        var details = (java.util.List<Map<String, Object>>) response.get("details");
        assertThat(details).isEmpty();
    }

    @Test
    void handleNotFound_ShouldReturnNotFoundResponse() {
        // Given
        EntityNotFoundException exception = new EntityNotFoundException("Product not found");

        // When
        Map<String, Object> response = exceptionHandler.handleNotFound(exception);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.get("error")).isEqualTo("Not Found");
        assertThat(response.get("message")).isEqualTo("Product not found");
    }


    @Test
    void handleIllegalArgument_ShouldReturnBadRequestResponse() {
        // Given
        IllegalArgumentException exception = new IllegalArgumentException("Invalid category ID");

        // When
        Map<String, Object> response = exceptionHandler.handleIllegalArgument(exception);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.get("error")).isEqualTo("Bad Request");
        assertThat(response.get("message")).isEqualTo("Invalid category ID");
    }


    @Test
    void handleIllegalArgument_WithEmptyMessage_ShouldReturnBadRequestResponse() {
        // Given
        IllegalArgumentException exception = new IllegalArgumentException("");

        // When
        Map<String, Object> response = exceptionHandler.handleIllegalArgument(exception);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.get("error")).isEqualTo("Bad Request");
        assertThat(response.get("message")).isEqualTo("");
    }
}