package se.moln.productservice.mappning;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.moln.productservice.dto.ProductRequest;
import se.moln.productservice.dto.ProductResponse;
import se.moln.productservice.model.Category;
import se.moln.productservice.model.Product;
import se.moln.productservice.model.ProductImage;
import java.util.UUID;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

class ProductMapperTest {

    private ProductMapper productMapper;
    private Category category;
    private ProductRequest productRequest;
    private Product product;

    @BeforeEach
    void setUp() {
        productMapper = new ProductMapper();
        
        category = new Category();
        category.setName("Electronics");
        category.setSlug("electronics");
        
        Map<String, String> attributes = new HashMap<>();
        attributes.put("color", "black");
        attributes.put("storage", "256GB");
        
        productRequest = new ProductRequest(
                "iPhone 15 Pro",
                "Latest iPhone with USB-C",
                BigDecimal.valueOf(12999.00),
                "SEK",
                UUID.randomUUID(),
                "Electronics",
                10,
                attributes,
                Arrays.asList("http://example.com/image1.jpg", "http://example.com/image2.jpg")
        );
        
        product = new Product();
        product.setId(UUID.randomUUID());
        product.setName("iPhone 15 Pro");
        product.setSlug("iphone-15-pro");
        product.setDescription("Latest iPhone with USB-C");
        product.setPrice(BigDecimal.valueOf(12999.00));
        product.setCurrency("SEK");
        product.setCategory(category);
        product.setStockQuantity(10);
        product.setActive(true);
        product.setAttributes(attributes);
    }

    @Test
    void toEntity_WithValidRequest_ShouldMapCorrectly() {
        // When
        Product result = productMapper.toEntity(productRequest, category);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("iPhone 15 Pro");
        assertThat(result.getSlug()).isEqualTo("iphone-15-pro");
        assertThat(result.getDescription()).isEqualTo("Latest iPhone with USB-C");
        assertThat(result.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(12999.00));
        assertThat(result.getCurrency()).isEqualTo("SEK");
        assertThat(result.getCategory()).isEqualTo(category);
        assertThat(result.getStockQuantity()).isEqualTo(10);
        assertThat(result.getAttributes()).containsEntry("color", "black");
        assertThat(result.getAttributes()).containsEntry("storage", "256GB");
        assertThat(result.getImages()).hasSize(2);
    }

    @Test
    void toEntity_WithNullStockQuantity_ShouldDefaultToZero() {
        // Given
        ProductRequest requestWithNullStock = new ProductRequest(
                "Test Product", "Description", BigDecimal.valueOf(100.00), "SEK",
                null, "Electronics", null, null, null
        );

        // When
        Product result = productMapper.toEntity(requestWithNullStock, category);

        // Then
        assertThat(result.getStockQuantity()).isEqualTo(0);
    }

    @Test
    void toEntity_WithNullAttributes_ShouldHandleGracefully() {
        // Given
        ProductRequest requestWithNullAttributes = new ProductRequest(
                "Test Product", "Description", BigDecimal.valueOf(100.00), "SEK",
                null, "Electronics", 5, null, null
        );

        // When
        Product result = productMapper.toEntity(requestWithNullAttributes, category);

        // Then
        // ProductMapper doesn't set attributes if null, so it uses the default empty map from Product constructor
        assertThat(result.getAttributes()).isEmpty();
    }

    @Test
    void toEntity_WithNullImageUrls_ShouldHandleGracefully() {
        // Given
        ProductRequest requestWithNullImages = new ProductRequest(
                "Test Product", "Description", BigDecimal.valueOf(100.00), "SEK",
                null, "Electronics", 5, new HashMap<>(), null
        );

        // When
        Product result = productMapper.toEntity(requestWithNullImages, category);

        // Then
        assertThat(result.getImages()).isEmpty();
    }

    @Test
    void toEntity_WithEmptyImageUrls_ShouldHandleGracefully() {
        // Given
        ProductRequest requestWithEmptyImages = new ProductRequest(
                "Test Product", "Description", BigDecimal.valueOf(100.00), "SEK",
                null, "Electronics", 5, new HashMap<>(), Arrays.asList()
        );

        // When
        Product result = productMapper.toEntity(requestWithEmptyImages, category);

        // Then
        assertThat(result.getImages()).isEmpty();
    }

    @Test
    void toResponse_WithValidProduct_ShouldMapCorrectly() {
        // Given
        ProductImage image1 = new ProductImage("http://example.com/image1.jpg", "image1.jpg", "image/jpeg", 1024L);
        ProductImage image2 = new ProductImage("http://example.com/image2.jpg", "image2.jpg", "image/jpeg", 2048L);
        product.getImages().addAll(Arrays.asList(image1, image2));

        // When
        ProductResponse result = productMapper.toResponse(product);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(product.getId());
        assertThat(result.name()).isEqualTo("iPhone 15 Pro");
        assertThat(result.slug()).isEqualTo("iphone-15-pro");
        assertThat(result.description()).isEqualTo("Latest iPhone with USB-C");
        assertThat(result.price()).isEqualByComparingTo(BigDecimal.valueOf(12999.00));
        assertThat(result.currency()).isEqualTo("SEK");
        assertThat(result.categoryName()).isEqualTo("Electronics");
        assertThat(result.stockQuantity()).isEqualTo(10);
        assertThat(result.active()).isTrue();
        assertThat(result.attributes()).containsEntry("color", "black");
        assertThat(result.attributes()).containsEntry("storage", "256GB");
        assertThat(result.images()).containsExactly(
                "http://example.com/image1.jpg", "http://example.com/image2.jpg"
        );
    }

    @Test
    void toResponse_WithNullCategory_ShouldReturnNullCategoryName() {
        // Given
        product.setCategory(null);

        // When
        ProductResponse result = productMapper.toResponse(product);

        // Then
        assertThat(result.categoryName()).isNull();
    }

    @Test
    void toResponse_WithNullAttributes_ShouldReturnEmptyMap() {
        // Given
        product.setAttributes(null);

        // When
        ProductResponse result = productMapper.toResponse(product);

        // Then
        assertThat(result.attributes()).isEmpty();
    }

    @Test
    void toResponse_WithNullImages_ShouldReturnEmptyList() {
        // Given
        product.setImages(null);

        // When
        ProductResponse result = productMapper.toResponse(product);

        // Then
        assertThat(result.images()).isEmpty();
    }

    @Test
    void toResponse_WithEmptyImages_ShouldReturnEmptyList() {
        // Given
        product.getImages().clear();

        // When
        ProductResponse result = productMapper.toResponse(product);

        // Then
        assertThat(result.images()).isEmpty();
    }

    @Test
    void slugify_WithNormalString_ShouldCreateValidSlug() {
        // Given
        ProductRequest request = new ProductRequest(
                "iPhone 15 Pro Max", "Description", BigDecimal.valueOf(100.00), "SEK",
                null, "Electronics", 5, null, null
        );

        // When
        Product result = productMapper.toEntity(request, category);

        // Then
        assertThat(result.getSlug()).isEqualTo("iphone-15-pro-max");
    }

    @Test
    void slugify_WithSpecialCharacters_ShouldCreateValidSlug() {
        // Given
        ProductRequest request = new ProductRequest(
                "iPhone 15 Pro!!! @#$%^&*()", "Description", BigDecimal.valueOf(100.00), "SEK",
                null, "Electronics", 5, null, null
        );

        // When
        Product result = productMapper.toEntity(request, category);

        // Then
        assertThat(result.getSlug()).isEqualTo("iphone-15-pro");
    }

    @Test
    void slugify_WithLeadingAndTrailingSpaces_ShouldTrim() {
        // Given
        ProductRequest request = new ProductRequest(
                "  iPhone 15 Pro  ", "Description", BigDecimal.valueOf(100.00), "SEK",
                null, "Electronics", 5, null, null
        );

        // When
        Product result = productMapper.toEntity(request, category);

        // Then
        assertThat(result.getSlug()).isEqualTo("iphone-15-pro");
    }

    @Test
    void slugify_WithMultipleSpaces_ShouldReplaceWithSingleDash() {
        // Given
        ProductRequest request = new ProductRequest(
                "iPhone    15    Pro", "Description", BigDecimal.valueOf(100.00), "SEK",
                null, "Electronics", 5, null, null
        );

        // When
        Product result = productMapper.toEntity(request, category);

        // Then
        assertThat(result.getSlug()).isEqualTo("iphone-15-pro");
    }

    @Test
    void slugify_WithNumbers_ShouldPreserveNumbers() {
        // Given
        ProductRequest request = new ProductRequest(
                "iPhone 15 Pro 256GB", "Description", BigDecimal.valueOf(100.00), "SEK",
                null, "Electronics", 5, null, null
        );

        // When
        Product result = productMapper.toEntity(request, category);

        // Then
        assertThat(result.getSlug()).isEqualTo("iphone-15-pro-256gb");
    }

    @Test
    void slugify_WithMixedCase_ShouldConvertToLowercase() {
        // Given
        ProductRequest request = new ProductRequest(
                "iPhone PRO Max", "Description", BigDecimal.valueOf(100.00), "SEK",
                null, "Electronics", 5, null, null
        );

        // When
        Product result = productMapper.toEntity(request, category);

        // Then
        assertThat(result.getSlug()).isEqualTo("iphone-pro-max");
    }

    @Test
    void toEntity_WithEmptyAttributes_ShouldCreateEmptyMap() {
        // Given
        ProductRequest requestWithEmptyAttributes = new ProductRequest(
                "Test Product", "Description", BigDecimal.valueOf(100.00), "SEK",
                null, "Electronics", 5, new HashMap<>(), null
        );

        // When
        Product result = productMapper.toEntity(requestWithEmptyAttributes, category);

        // Then
        assertThat(result.getAttributes()).isEmpty();
    }

    @Test
    void toResponse_PreservesAttributeOrder() {
        // Given
        Map<String, String> orderedAttributes = new LinkedHashMap<>();
        orderedAttributes.put("color", "black");
        orderedAttributes.put("storage", "256GB");
        orderedAttributes.put("ram", "8GB");
        product.setAttributes(orderedAttributes);

        // When
        ProductResponse result = productMapper.toResponse(product);

        // Then
        assertThat(result.attributes()).containsExactly(
                Map.entry("color", "black"),
                Map.entry("storage", "256GB"),
                Map.entry("ram", "8GB")
        );
    }
}