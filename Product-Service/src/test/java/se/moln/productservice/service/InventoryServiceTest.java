package se.moln.productservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.moln.productservice.dto.InventoryResponse;
import se.moln.productservice.dto.ProductResponse;
import se.moln.productservice.dto.PurchaseResponse;
import se.moln.productservice.mappning.ProductMapper;
import se.moln.productservice.model.Product;
import se.moln.productservice.repository.ProductRepository;
import se.moln.productservice.exception.ProductNotFoundException;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock private ProductRepository productRepository;
    @Mock private ProductMapper productMapper;

    @InjectMocks private InventoryService service;

    private UUID productId;
    private Product product;
    private ProductResponse productResponse;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        product = new Product();
        product.setId(productId);
        product.setName("Test Product");
        product.setDescription("desc");
        product.setPrice(BigDecimal.TEN);
        product.setCurrency("SEK");
        product.setStockQuantity(5);

        productResponse = new ProductResponse(
                productId,
                "Test Product",
                "test-product",
                "desc",
                BigDecimal.TEN,
                "SEK",
                null,
                5,
                true,
                java.util.Map.of(),
                java.util.List.of()
        );
    }

    @Test
    void purchase_IllegalState_WrappedAsInsufficientStock() {
        Product spyProduct = org.mockito.Mockito.spy(product);
        org.mockito.Mockito.doThrow(new IllegalStateException("legacy illegal state"))
                .when(spyProduct).reserveStock(1);

        when(productRepository.findById(productId)).thenReturn(Optional.of(spyProduct));

        assertThatThrownBy(() -> service.purchase(productId, 1))
                .isInstanceOf(se.moln.productservice.exception.InsufficientStockException.class)
                .hasMessageContaining("legacy illegal state");
        verify(productRepository, never()).save(any());
    }

    @Test
    void get_ReturnsProductResponse() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productMapper.toResponse(eq(product))).thenReturn(productResponse);

        ProductResponse resp = service.get(productId);

        assertThat(resp).isNotNull();
        assertThat(resp.id()).isEqualTo(productId);
        assertThat(resp.stockQuantity()).isEqualTo(5);
    }

    @Test
    void get_WhenProductMissing_Throws() {
        when(productRepository.findById(productId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.get(productId))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("Product not found");
    }

    @Test
    void purchase_WithEnoughStock_DecreasesQuantity() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productMapper.toResponse(any(Product.class))).thenReturn(productResponse);

        PurchaseResponse resp = service.purchase(productId, 3);

        assertThat(resp.purchasedQuantity()).isEqualTo(3);
        assertThat(resp.newStockQuantity()).isEqualTo(2);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void purchase_ExactStock_ReachesZero() {
        product.setStockQuantity(2);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productMapper.toResponse(any(Product.class))).thenReturn(productResponse);

        PurchaseResponse resp = service.purchase(productId, 2);

        assertThat(resp.newStockQuantity()).isZero();
    }

    @Test
    void purchase_NotEnoughStock_Throws() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        assertThatThrownBy(() -> service.purchase(productId, 10))
                .isInstanceOf(se.moln.productservice.exception.InsufficientStockException.class)
                .hasMessageContaining("Otillräckligt lager");
        verify(productRepository, never()).save(any());
    }

    @Test
    void purchase_InternalIndexIssue_WrappedAsInsufficientStock() {
        // Simulate an unexpected IndexOutOfBoundsException from reserveStock()
        Product spyProduct = org.mockito.Mockito.spy(product);
        org.mockito.Mockito.doThrow(new IndexOutOfBoundsException("bad index"))
                .when(spyProduct).reserveStock(1);

        when(productRepository.findById(productId)).thenReturn(Optional.of(spyProduct));

        assertThatThrownBy(() -> service.purchase(productId, 1))
                .isInstanceOf(se.moln.productservice.exception.InsufficientStockException.class)
                .hasMessageContaining("bad index");
        verify(productRepository, never()).save(any());
    }

    @Test
    void purchase_InvalidQuantity_Throws() {
        assertThatThrownBy(() -> service.purchase(productId, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("quantity must be > 0");
    }

    @Test
    void refund_IncreasesQuantityAndSetsInStock() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        InventoryResponse resp = service.refund(productId, 4);

        assertThat(resp.quantity()).isEqualTo(9);
        assertThat(resp.message()).isEqualTo("Return successful.");
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void refund_InvalidQuantity_Throws() {
        assertThatThrownBy(() -> service.refund(productId, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("quantity must be > 0");
    }
}
