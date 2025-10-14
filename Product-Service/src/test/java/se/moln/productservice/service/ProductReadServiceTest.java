package se.moln.productservice.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.moln.productservice.dto.ProductResponse;
import se.moln.productservice.mappning.ProductMapper;
import se.moln.productservice.model.Product;
import se.moln.productservice.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductReadServiceTest {

    @Mock private ProductRepository repo;
    @Mock private ProductMapper mapper;

    @InjectMocks private ProductReadService service;

    private UUID id;
    private Product entity;
    private ProductResponse dto;

    @BeforeEach
    void setup() {
        id = UUID.randomUUID();
        entity = new Product();
        entity.setId(id);
        entity.setName("Test Product");

        dto = new ProductResponse(
                id, "Test Product", "test-product", "Desc",
                BigDecimal.valueOf(99.99), "SEK", "Electronics", 10, true,
                Map.of(), List.of()
        );
    }

    @Test
    void getById_ShouldReturnResponse() {
        when(repo.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toResponse(entity)).thenReturn(dto);

        ProductResponse out = service.getById(id);
        assertThat(out).isNotNull();
        assertThat(out.id()).isEqualTo(id);
    }

    @Test
    void getById_NotFound_ShouldThrow() {
        when(repo.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Product not found");
    }
}
