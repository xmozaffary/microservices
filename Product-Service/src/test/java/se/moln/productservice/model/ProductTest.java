package se.moln.productservice.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    private Product product;
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setName("Electronics");
        category.setSlug("electronics");

        product = new Product();
        product.setId(UUID.randomUUID());
        product.setName("iPhone 15 Pro");
        product.setSlug("iphone-15-pro");
        product.setDescription("Latest iPhone model");
        product.setPrice(BigDecimal.valueOf(12999.00));
        product.setCurrency("SEK");
        product.setCategory(category);
        product.setStockQuantity(50);
        product.setActive(true);
    }

    @Test
    void reserveStock_WithValidQuantity_ShouldReduceStock() {
        // Given
        int initialStock = product.getStockQuantity();
        int reserveQuantity = 5;

        // When
        product.reserveStock(reserveQuantity);

        // Then
        assertThat(product.getStockQuantity()).isEqualTo(initialStock - reserveQuantity);
    }

    @Test
    void reserveStock_WithZeroQuantity_ShouldThrowIllegalArgumentException() {
        // When & Then
        assertThatThrownBy(() -> product.reserveStock(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("quantity måste vara > 0");
    }

    @Test
    void reserveStock_WithNegativeQuantity_ShouldThrowIllegalArgumentException() {
        // When & Then
        assertThatThrownBy(() -> product.reserveStock(-5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("quantity måste vara > 0");
    }

    @Test
    void reserveStock_WithInsufficientStock_ShouldThrowIllegalStateException() {
        // Given
        product.setStockQuantity(10);

        // When & Then
        assertThatThrownBy(() -> product.reserveStock(15))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Otillräckligt lager");
    }

    @Test
    void reserveStock_WithExactStock_ShouldSetStockToZero() {
        // Given
        product.setStockQuantity(10);

        // When
        product.reserveStock(10);

        // Then
        assertThat(product.getStockQuantity()).isEqualTo(0);
    }

    @Test
    void releaseStock_WithValidQuantity_ShouldIncreaseStock() {
        // Given
        int initialStock = product.getStockQuantity();
        int releaseQuantity = 5;

        // When
        product.releaseStock(releaseQuantity);

        // Then
        assertThat(product.getStockQuantity()).isEqualTo(initialStock + releaseQuantity);
    }

    @Test
    void releaseStock_WithZeroQuantity_ShouldThrowIllegalArgumentException() {
        // When & Then
        assertThatThrownBy(() -> product.releaseStock(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("quantity måste vara > 0");
    }

    @Test
    void releaseStock_WithNegativeQuantity_ShouldThrowIllegalArgumentException() {
        // When & Then
        assertThatThrownBy(() -> product.releaseStock(-5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("quantity måste vara > 0");
    }

    @Test
    void addImage_WithValidParameters_ShouldAddImageToList() {
        // Given
        String url = "http://example.com/image.jpg";
        String fileName = "image.jpg";
        String contentType = "image/jpeg";
        Long sizeBytes = 1024L;

        // When
        product.addImage(url, fileName, contentType, sizeBytes);

        // Then
        assertThat(product.getImages()).hasSize(1);
        ProductImage addedImage = product.getImages().get(0);
        assertThat(addedImage.getUrl()).isEqualTo(url);
        assertThat(addedImage.getFileName()).isEqualTo(fileName);
        assertThat(addedImage.getContentType()).isEqualTo(contentType);
        assertThat(addedImage.getSizeBytes()).isEqualTo(sizeBytes);
    }

    @Test
    void addImage_WithNullParameters_ShouldHandleGracefully() {
        // When
        product.addImage("http://example.com/image.jpg", null, null, null);

        // Then
        assertThat(product.getImages()).hasSize(1);
        ProductImage addedImage = product.getImages().get(0);
        assertThat(addedImage.getUrl()).isEqualTo("http://example.com/image.jpg");
        assertThat(addedImage.getFileName()).isNull();
        assertThat(addedImage.getContentType()).isNull();
        assertThat(addedImage.getSizeBytes()).isNull();
    }

    @Test
    void addImage_MultipleImages_ShouldMaintainOrder() {
        // When
        product.addImage("http://example.com/image1.jpg", "image1.jpg", "image/jpeg", 1024L);
        product.addImage("http://example.com/image2.jpg", "image2.jpg", "image/png", 2048L);
        product.addImage("http://example.com/image3.jpg", "image3.jpg", "image/webp", 512L);

        // Then
        assertThat(product.getImages()).hasSize(3);
        assertThat(product.getImages().get(0).getUrl()).isEqualTo("http://example.com/image1.jpg");
        assertThat(product.getImages().get(1).getUrl()).isEqualTo("http://example.com/image2.jpg");
        assertThat(product.getImages().get(2).getUrl()).isEqualTo("http://example.com/image3.jpg");
    }

    @Test
    void putAttribute_WithValidKeyValue_ShouldAddAttribute() {
        // Given
        String key = "color";
        String value = "black";

        // When
        product.putAttribute(key, value);

        // Then
        assertThat(product.getAttributes()).containsEntry(key, value);
    }

    @Test
    void putAttribute_WithNullKey_ShouldThrowNullPointerException() {
        // When & Then
        assertThatThrownBy(() -> product.putAttribute(null, "value"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void putAttribute_WithNullValue_ShouldAddNullValue() {
        // Given
        String key = "color";

        // When
        product.putAttribute(key, null);

        // Then
        assertThat(product.getAttributes()).containsEntry(key, null);
    }

    @Test
    void putAttribute_WithExistingKey_ShouldReplaceValue() {
        // Given
        String key = "color";
        product.putAttribute(key, "black");

        // When
        product.putAttribute(key, "white");

        // Then
        assertThat(product.getAttributes()).containsEntry(key, "white");
        assertThat(product.getAttributes()).hasSize(1);
    }

    @Test
    void putAttribute_MultipleAttributes_ShouldMaintainOrder() {
        // When
        product.putAttribute("color", "black");
        product.putAttribute("storage", "256GB");
        product.putAttribute("ram", "8GB");

        // Then
        assertThat(product.getAttributes()).hasSize(3);
        assertThat(product.getAttributes().keySet()).containsExactly("color", "storage", "ram");
    }

    @Test
    void equals_WithSameId_ShouldReturnTrue() {
        // Given
        UUID id = UUID.randomUUID();
        Product product1 = new Product();
        product1.setId(id);
        Product product2 = new Product();
        product2.setId(id);

        // When & Then
        assertThat(product1).isEqualTo(product2);
    }

    @Test
    void equals_WithDifferentId_ShouldReturnFalse() {
        // Given
        Product product1 = new Product();
        product1.setId(UUID.randomUUID());
        Product product2 = new Product();
        product2.setId(UUID.randomUUID());

        // When & Then
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    void equals_WithNullId_ShouldReturnFalse() {
        // Given
        Product product1 = new Product();
        product1.setId(null);
        Product product2 = new Product();
        product2.setId(UUID.randomUUID());

        // When & Then
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    void equals_WithSameInstance_ShouldReturnTrue() {
        // When & Then
        assertThat(product).isEqualTo(product);
    }

    @Test
    void equals_WithDifferentClass_ShouldReturnFalse() {
        // When & Then
        assertThat(product).isNotEqualTo("not a product");
    }

    @Test
    void hashCode_WithSameId_ShouldReturnSameHashCode() {
        // Given
        UUID id = UUID.randomUUID();
        Product product1 = new Product();
        product1.setId(id);
        Product product2 = new Product();
        product2.setId(id);

        // When & Then
        assertThat(product1.hashCode()).isEqualTo(product2.hashCode());
    }

    @Test
    void hashCode_WithNullId_ShouldReturnZero() {
        // Given
        Product productWithNullId = new Product();
        productWithNullId.setId(null);

        // When & Then
        assertThat(productWithNullId.hashCode()).isEqualTo(0);
    }

    @Test
    void defaultValues_ShouldBeSetCorrectly() {
        // Given
        Product newProduct = new Product();

        // Then
        assertThat(newProduct.getStockQuantity()).isEqualTo(0);
        assertThat(newProduct.isActive()).isTrue();
        assertThat(newProduct.getImages()).isEmpty();
        assertThat(newProduct.getAttributes()).isEmpty();
    }

    @Test
    void gettersAndSetters_ShouldWorkCorrectly() {
        // Given
        UUID id = UUID.randomUUID();
        String name = "Test Product";
        String slug = "test-product";
        String description = "Test description";
        BigDecimal price = BigDecimal.valueOf(999.99);
        String currency = "USD";
        int stockQuantity = 100;
        boolean active = false;

        // When
        product.setId(id);
        product.setName(name);
        product.setSlug(slug);
        product.setDescription(description);
        product.setPrice(price);
        product.setCurrency(currency);
        product.setCategory(category);
        product.setStockQuantity(stockQuantity);
        product.setActive(active);

        // Then
        assertThat(product.getId()).isEqualTo(id);
        assertThat(product.getName()).isEqualTo(name);
        assertThat(product.getSlug()).isEqualTo(slug);
        assertThat(product.getDescription()).isEqualTo(description);
        assertThat(product.getPrice()).isEqualTo(price);
        assertThat(product.getCurrency()).isEqualTo(currency);
        assertThat(product.getCategory()).isEqualTo(category);
        assertThat(product.getStockQuantity()).isEqualTo(stockQuantity);
        assertThat(product.isActive()).isEqualTo(active);
    }

    @Test
    void stockOperations_ComplexScenario_ShouldWorkCorrectly() {
        // Given
        product.setStockQuantity(100);

        // When & Then
        product.reserveStock(30);
        assertThat(product.getStockQuantity()).isEqualTo(70);

        product.reserveStock(20);
        assertThat(product.getStockQuantity()).isEqualTo(50);

        product.releaseStock(10);
        assertThat(product.getStockQuantity()).isEqualTo(60);

        product.releaseStock(40);
        assertThat(product.getStockQuantity()).isEqualTo(100);
    }
}