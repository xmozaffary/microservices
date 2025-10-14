package se.moln.productservice.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryTest {

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setName("Electronics");
        category.setSlug("electronics");
        category.setDescription("Electronic devices and gadgets");
    }

    @Test
    void gettersAndSetters_ShouldWorkCorrectly() {
        // Given
        String name = "Books";
        String slug = "books";
        String description = "Books and literature";

        // When
        category.setName(name);
        category.setSlug(slug);
        category.setDescription(description);

        // Then
        assertThat(category.getName()).isEqualTo(name);
        assertThat(category.getSlug()).isEqualTo(slug);
        assertThat(category.getDescription()).isEqualTo(description);
    }

    @Test
    void getId_ShouldReturnNullForNewCategory() {
        // Given
        Category newCategory = new Category();

        // Then
        assertThat(newCategory.getId()).isNull();
    }

    @Test
    void equals_WithSameId_ShouldReturnTrue() {
        // Given
        UUID id = UUID.randomUUID();
        Category category1 = new Category();
        category1.setName("Category 1");
        // Simulate JPA setting the ID
        setId(category1, id);
        
        Category category2 = new Category();
        category2.setName("Category 2");
        setId(category2, id);

        // When & Then
        assertThat(category1).isEqualTo(category2);
    }

    @Test
    void equals_WithDifferentId_ShouldReturnFalse() {
        // Given
        Category category1 = new Category();
        setId(category1, UUID.randomUUID());
        
        Category category2 = new Category();
        setId(category2, UUID.randomUUID());

        // When & Then
        assertThat(category1).isNotEqualTo(category2);
    }

    @Test
    void equals_WithNullId_ShouldReturnFalse() {
        // Given
        Category category1 = new Category();
        setId(category1, null);
        
        Category category2 = new Category();
        setId(category2, UUID.randomUUID());

        // When & Then
        assertThat(category1).isNotEqualTo(category2);
    }

    @Test
    void equals_WithBothNullIds_ShouldReturnFalse() {
        // Given
        Category category1 = new Category();
        setId(category1, null);
        
        Category category2 = new Category();
        setId(category2, null);

        // When & Then
        assertThat(category1).isNotEqualTo(category2);
    }

    @Test
    void equals_WithSameInstance_ShouldReturnTrue() {
        // When & Then
        assertThat(category).isEqualTo(category);
    }

    @Test
    void equals_WithNull_ShouldReturnFalse() {
        // When & Then
        assertThat(category).isNotEqualTo(null);
    }

    @Test
    void equals_WithDifferentClass_ShouldReturnFalse() {
        // When & Then
        assertThat(category).isNotEqualTo("not a category");
    }

    @Test
    void hashCode_WithSameId_ShouldReturnSameHashCode() {
        // Given
        UUID id = UUID.randomUUID();
        Category category1 = new Category();
        setId(category1, id);
        
        Category category2 = new Category();
        setId(category2, id);

        // When & Then
        assertThat(category1.hashCode()).isEqualTo(category2.hashCode());
    }

    @Test
    void hashCode_WithNullId_ShouldReturnZero() {
        // Given
        Category categoryWithNullId = new Category();
        setId(categoryWithNullId, null);

        // When & Then
        assertThat(categoryWithNullId.hashCode()).isEqualTo(0);
    }

    @Test
    void hashCode_WithDifferentIds_ShouldReturnDifferentHashCodes() {
        // Given
        Category category1 = new Category();
        setId(category1, UUID.randomUUID());
        
        Category category2 = new Category();
        setId(category2, UUID.randomUUID());

        // When & Then
        assertThat(category1.hashCode()).isNotEqualTo(category2.hashCode());
    }

    @Test
    void setName_WithValidName_ShouldSetCorrectly() {
        // Given
        String name = "Home & Garden";

        // When
        category.setName(name);

        // Then
        assertThat(category.getName()).isEqualTo(name);
    }

    @Test
    void setSlug_WithValidSlug_ShouldSetCorrectly() {
        // Given
        String slug = "home-and-garden";

        // When
        category.setSlug(slug);

        // Then
        assertThat(category.getSlug()).isEqualTo(slug);
    }

    @Test
    void setDescription_WithValidDescription_ShouldSetCorrectly() {
        // Given
        String description = "Home improvement and gardening products";

        // When
        category.setDescription(description);

        // Then
        assertThat(category.getDescription()).isEqualTo(description);
    }

    @Test
    void setDescription_WithNullDescription_ShouldSetNull() {
        // When
        category.setDescription(null);

        // Then
        assertThat(category.getDescription()).isNull();
    }

    @Test
    void setDescription_WithEmptyDescription_ShouldSetEmpty() {
        // When
        category.setDescription("");

        // Then
        assertThat(category.getDescription()).isEmpty();
    }

    @Test
    void toString_ShouldNotThrowException() {
        // Given
        Category categoryWithId = new Category();
        categoryWithId.setName("Test Category");
        categoryWithId.setSlug("test-category");
        setId(categoryWithId, UUID.randomUUID());

        // When & Then - just verify toString doesn't throw exception
        String result = categoryWithId.toString();
        assertThat(result).isNotNull();
    }

    @Test
    void newCategory_ShouldHaveNullValues() {
        // Given
        Category newCategory = new Category();

        // Then
        assertThat(newCategory.getId()).isNull();
        assertThat(newCategory.getName()).isNull();
        assertThat(newCategory.getSlug()).isNull();
        assertThat(newCategory.getDescription()).isNull();
    }

    @Test
    void categoryComparison_BasedOnIdOnly_ShouldIgnoreOtherFields() {
        // Given
        UUID id = UUID.randomUUID();
        Category category1 = new Category();
        category1.setName("Electronics");
        category1.setSlug("electronics");
        category1.setDescription("Electronic devices");
        setId(category1, id);

        Category category2 = new Category();
        category2.setName("Computers");
        category2.setSlug("computers");
        category2.setDescription("Computer equipment");
        setId(category2, id);

        // When & Then - Should be equal despite different name, slug, description
        assertThat(category1).isEqualTo(category2);
        assertThat(category1.hashCode()).isEqualTo(category2.hashCode());
    }

    @Test
    void setName_WithLongName_ShouldSetCorrectly() {
        // Given
        String longName = "A".repeat(100); // Maximum length according to @Size(max = 100)

        // When
        category.setName(longName);

        // Then
        assertThat(category.getName()).isEqualTo(longName);
        assertThat(category.getName()).hasSize(100);
    }

    @Test
    void setSlug_WithLongSlug_ShouldSetCorrectly() {
        // Given
        String longSlug = "a-".repeat(100); // Creates "a-a-a-..." up to 200 chars

        // When
        category.setSlug(longSlug);

        // Then
        assertThat(category.getSlug()).isEqualTo(longSlug);
    }

    @Test
    void setDescription_WithLongDescription_ShouldSetCorrectly() {
        // Given
        String longDescription = "A".repeat(500); // Maximum length according to @Size(max = 500)

        // When
        category.setDescription(longDescription);

        // Then
        assertThat(category.getDescription()).isEqualTo(longDescription);
        assertThat(category.getDescription()).hasSize(500);
    }

    // Helper method to set ID via reflection since there's no setter
    private void setId(Category category, UUID id) {
        try {
            var field = Category.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(category, id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set ID", e);
        }
    }
}