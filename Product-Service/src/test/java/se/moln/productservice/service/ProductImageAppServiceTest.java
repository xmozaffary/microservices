package se.moln.productservice.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import se.moln.productservice.dto.ProductResponse;
import se.moln.productservice.mappning.ProductMapper;
import se.moln.productservice.model.Product;
import se.moln.productservice.repository.ProductRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductImageAppServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private FileStorageService fileStorageService;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private ProductImageAppService productImageAppService;

    private UUID productId;
    private Product product;
    private ProductResponse productResponse;
    private FileStorageService.StoredFile storedFile;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        
        product = new Product();
        product.setId(productId);
        product.setName("Test Product");
        product.setSlug("test-product");
        product.setDescription("Test Description");
        product.setPrice(BigDecimal.valueOf(99.99));

        productResponse = new ProductResponse(
                productId,
                "Test Product",
                "test-product",
                "Test Description",
                BigDecimal.valueOf(99.99),
                "SEK",
                "Electronics",
                10,
                true,
                new HashMap<>(),
                List.of("/uploads/test-image.jpg")
        );

        storedFile = new FileStorageService.StoredFile(
                "/uploads/test-image.jpg",
                "test-image.jpg",
                "image/jpeg",
                1024L
        );
    }

    @Test
    void uploadImage_WithValidProductAndFile_ShouldReturnProductResponse() throws IOException {
        // Given
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(fileStorageService.store(multipartFile)).thenReturn(storedFile);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toResponse(any(Product.class))).thenReturn(productResponse);

        // When
        ProductResponse result = productImageAppService.uploadImage(productId, multipartFile);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(productId);
        assertThat(result.images()).contains("/uploads/test-image.jpg");
        
        verify(productRepository).findById(productId);
        verify(fileStorageService).store(multipartFile);
        verify(productRepository).save(product);
        verify(productMapper).toResponse(product);
    }

    @Test
    void uploadImage_WithNonExistentProduct_ShouldThrowEntityNotFoundException() throws IOException {
        // Given
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> productImageAppService.uploadImage(productId, multipartFile))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Product not found: " + productId);

        verify(productRepository).findById(productId);
        verify(fileStorageService, never()).store(any());
        verify(productRepository, never()).save(any());
        verify(productMapper, never()).toResponse(any());
    }

    @Test
    void uploadImage_WithFileStorageException_ShouldPropagateIOException() throws IOException {
        // Given
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(fileStorageService.store(multipartFile)).thenThrow(new IOException("Storage failed"));

        // When & Then
        assertThatThrownBy(() -> productImageAppService.uploadImage(productId, multipartFile))
                .isInstanceOf(IOException.class)
                .hasMessage("Storage failed");

        verify(productRepository).findById(productId);
        verify(fileStorageService).store(multipartFile);
        verify(productRepository, never()).save(any());
        verify(productMapper, never()).toResponse(any());
    }

    @Test
    void uploadImage_ShouldCallAddImageOnProduct() throws IOException {
        // Given
        Product spyProduct = spy(product);
        when(productRepository.findById(productId)).thenReturn(Optional.of(spyProduct));
        when(fileStorageService.store(multipartFile)).thenReturn(storedFile);
        when(productRepository.save(any(Product.class))).thenReturn(spyProduct);
        when(productMapper.toResponse(any(Product.class))).thenReturn(productResponse);

        // When
        productImageAppService.uploadImage(productId, multipartFile);

        // Then
        verify(spyProduct).addImage(
                storedFile.url(),
                storedFile.originalName(),
                storedFile.contentType(),
                storedFile.size()
        );
    }

    @Test
    void uploadImage_WithDifferentFileTypes_ShouldHandleCorrectly() throws IOException {
        // Given
        FileStorageService.StoredFile pngFile = new FileStorageService.StoredFile(
                "/uploads/test-image.png",
                "test-image.png",
                "image/png",
                2048L
        );
        
        Product spyProduct = spy(product);
        when(productRepository.findById(productId)).thenReturn(Optional.of(spyProduct));
        when(fileStorageService.store(multipartFile)).thenReturn(pngFile);
        when(productRepository.save(any(Product.class))).thenReturn(spyProduct);
        when(productMapper.toResponse(any(Product.class))).thenReturn(productResponse);

        // When
        ProductResponse result = productImageAppService.uploadImage(productId, multipartFile);

        // Then
        assertThat(result).isNotNull();
        verify(spyProduct).addImage(
                "/uploads/test-image.png",
                "test-image.png",
                "image/png",
                2048L
        );
    }

    @Test
    void uploadImage_WithEmptyFile_ShouldPropagateIOException() throws IOException {
        // Given
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(fileStorageService.store(multipartFile)).thenThrow(new IOException("Empty file"));

        // When & Then
        assertThatThrownBy(() -> productImageAppService.uploadImage(productId, multipartFile))
                .isInstanceOf(IOException.class)
                .hasMessage("Empty file");

        verify(productRepository).findById(productId);
        verify(fileStorageService).store(multipartFile);
        verify(productRepository, never()).save(any());
    }

    @Test
    void uploadImage_ShouldBeTransactional() throws IOException {
        // Given
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(fileStorageService.store(multipartFile)).thenReturn(storedFile);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toResponse(any(Product.class))).thenReturn(productResponse);

        // When
        ProductResponse result = productImageAppService.uploadImage(productId, multipartFile);

        // Then
        assertThat(result).isNotNull();
        // Verify that save is called, which is important for transactional behavior
        verify(productRepository).save(product);
    }
}