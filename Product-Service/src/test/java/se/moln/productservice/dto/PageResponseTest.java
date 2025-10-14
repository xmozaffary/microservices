package se.moln.productservice.dto;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PageResponseTest {

    @Test
    void defaultConstructor_ShouldCreateEmptyPageResponse() {
        // When
        PageResponse<String> pageResponse = new PageResponse<>();

        // Then
        assertThat(pageResponse).isNotNull();
        assertThat(pageResponse.getContent()).isNull();
        assertThat(pageResponse.getPageNumber()).isEqualTo(0);
        assertThat(pageResponse.getPageSize()).isEqualTo(0);
        assertThat(pageResponse.getTotalElements()).isEqualTo(0L);
        assertThat(pageResponse.getTotalPages()).isEqualTo(0);
        assertThat(pageResponse.isFirst()).isFalse();
        assertThat(pageResponse.isLast()).isFalse();
        assertThat(pageResponse.isEmpty()).isFalse();
    }

    @Test
    void pageConstructor_WithValidPage_ShouldMapAllProperties() {
        // Given
        List<String> content = Arrays.asList("item1", "item2", "item3");
        Pageable pageable = PageRequest.of(1, 2);
        Page<String> page = new PageImpl<>(content, pageable, 10);

        // When
        PageResponse<String> pageResponse = new PageResponse<>(page);

        // Then
        assertThat(pageResponse.getContent()).containsExactly("item1", "item2", "item3");
        assertThat(pageResponse.getPageNumber()).isEqualTo(1);
        assertThat(pageResponse.getPageSize()).isEqualTo(2);
        assertThat(pageResponse.getTotalElements()).isEqualTo(10L);
        assertThat(pageResponse.getTotalPages()).isEqualTo(5);
        assertThat(pageResponse.isFirst()).isFalse();
        assertThat(pageResponse.isLast()).isFalse();
        assertThat(pageResponse.isEmpty()).isFalse();
    }

    @Test
    void pageConstructor_WithFirstPage_ShouldSetFirstTrue() {
        // Given
        List<String> content = Arrays.asList("item1", "item2");
        Pageable pageable = PageRequest.of(0, 2);
        Page<String> page = new PageImpl<>(content, pageable, 10);

        // When
        PageResponse<String> pageResponse = new PageResponse<>(page);

        // Then
        assertThat(pageResponse.isFirst()).isTrue();
        assertThat(pageResponse.isLast()).isFalse();
    }

    @Test
    void pageConstructor_WithLastPage_ShouldSetLastTrue() {
        // Given
        List<String> content = Arrays.asList("item9", "item10");
        Pageable pageable = PageRequest.of(4, 2);
        Page<String> page = new PageImpl<>(content, pageable, 10);

        // When
        PageResponse<String> pageResponse = new PageResponse<>(page);

        // Then
        assertThat(pageResponse.isFirst()).isFalse();
        assertThat(pageResponse.isLast()).isTrue();
    }

    @Test
    void pageConstructor_WithSinglePage_ShouldSetBothFirstAndLastTrue() {
        // Given
        List<String> content = Arrays.asList("item1", "item2");
        Pageable pageable = PageRequest.of(0, 5);
        Page<String> page = new PageImpl<>(content, pageable, 2);

        // When
        PageResponse<String> pageResponse = new PageResponse<>(page);

        // Then
        assertThat(pageResponse.isFirst()).isTrue();
        assertThat(pageResponse.isLast()).isTrue();
        assertThat(pageResponse.getTotalPages()).isEqualTo(1);
    }

    @Test
    void pageConstructor_WithEmptyPage_ShouldSetEmptyTrue() {
        // Given
        List<String> content = Collections.emptyList();
        Pageable pageable = PageRequest.of(0, 5);
        Page<String> page = new PageImpl<>(content, pageable, 0);

        // When
        PageResponse<String> pageResponse = new PageResponse<>(page);

        // Then
        assertThat(pageResponse.isEmpty()).isTrue();
        assertThat(pageResponse.getContent()).isEmpty();
        assertThat(pageResponse.getTotalElements()).isEqualTo(0L);
        assertThat(pageResponse.getTotalPages()).isEqualTo(1);
    }

    @Test
    void setContent_ShouldUpdateContent() {
        // Given
        PageResponse<String> pageResponse = new PageResponse<>();
        List<String> newContent = Arrays.asList("new1", "new2");

        // When
        pageResponse.setContent(newContent);

        // Then
        assertThat(pageResponse.getContent()).isEqualTo(newContent);
    }

    @Test
    void setContent_WithNull_ShouldAcceptNull() {
        // Given
        PageResponse<String> pageResponse = new PageResponse<>();

        // When
        pageResponse.setContent(null);

        // Then
        assertThat(pageResponse.getContent()).isNull();
    }

    @Test
    void setPageNumber_ShouldUpdatePageNumber() {
        // Given
        PageResponse<String> pageResponse = new PageResponse<>();

        // When
        pageResponse.setPageNumber(5);

        // Then
        assertThat(pageResponse.getPageNumber()).isEqualTo(5);
    }

    @Test
    void setPageSize_ShouldUpdatePageSize() {
        // Given
        PageResponse<String> pageResponse = new PageResponse<>();

        // When
        pageResponse.setPageSize(20);

        // Then
        assertThat(pageResponse.getPageSize()).isEqualTo(20);
    }

    @Test
    void setTotalElements_ShouldUpdateTotalElements() {
        // Given
        PageResponse<String> pageResponse = new PageResponse<>();

        // When
        pageResponse.setTotalElements(1000L);

        // Then
        assertThat(pageResponse.getTotalElements()).isEqualTo(1000L);
    }

    @Test
    void setTotalPages_ShouldUpdateTotalPages() {
        // Given
        PageResponse<String> pageResponse = new PageResponse<>();

        // When
        pageResponse.setTotalPages(50);

        // Then
        assertThat(pageResponse.getTotalPages()).isEqualTo(50);
    }

    @Test
    void setFirst_ShouldUpdateFirst() {
        // Given
        PageResponse<String> pageResponse = new PageResponse<>();

        // When
        pageResponse.setFirst(true);

        // Then
        assertThat(pageResponse.isFirst()).isTrue();
    }

    @Test
    void setLast_ShouldUpdateLast() {
        // Given
        PageResponse<String> pageResponse = new PageResponse<>();

        // When
        pageResponse.setLast(true);

        // Then
        assertThat(pageResponse.isLast()).isTrue();
    }

    @Test
    void setEmpty_ShouldUpdateEmpty() {
        // Given
        PageResponse<String> pageResponse = new PageResponse<>();

        // When
        pageResponse.setEmpty(true);

        // Then
        assertThat(pageResponse.isEmpty()).isTrue();
    }

    @Test
    void pageResponse_WithComplexObjects_ShouldWorkCorrectly() {
        // Given
        class TestObject {
            private String value;
            public TestObject(String value) { this.value = value; }
            public String getValue() { return value; }
        }

        List<TestObject> content = Arrays.asList(
                new TestObject("test1"),
                new TestObject("test2")
        );
        Pageable pageable = PageRequest.of(0, 2);
        Page<TestObject> page = new PageImpl<>(content, pageable, 5);

        // When
        PageResponse<TestObject> pageResponse = new PageResponse<>(page);

        // Then
        assertThat(pageResponse.getContent()).hasSize(2);
        assertThat(pageResponse.getContent().get(0).getValue()).isEqualTo("test1");
        assertThat(pageResponse.getContent().get(1).getValue()).isEqualTo("test2");
    }

    @Test
    void allSetters_ShouldWorkIndependently() {
        // Given
        PageResponse<String> pageResponse = new PageResponse<>();

        // When
        pageResponse.setContent(Arrays.asList("item1", "item2"));
        pageResponse.setPageNumber(3);
        pageResponse.setPageSize(10);
        pageResponse.setTotalElements(100L);
        pageResponse.setTotalPages(10);
        pageResponse.setFirst(false);
        pageResponse.setLast(false);
        pageResponse.setEmpty(false);

        // Then
        assertThat(pageResponse.getContent()).containsExactly("item1", "item2");
        assertThat(pageResponse.getPageNumber()).isEqualTo(3);
        assertThat(pageResponse.getPageSize()).isEqualTo(10);
        assertThat(pageResponse.getTotalElements()).isEqualTo(100L);
        assertThat(pageResponse.getTotalPages()).isEqualTo(10);
        assertThat(pageResponse.isFirst()).isFalse();
        assertThat(pageResponse.isLast()).isFalse();
        assertThat(pageResponse.isEmpty()).isFalse();
    }

    @Test
    void pageConstructor_WithZeroTotalElements_ShouldHandleCorrectly() {
        // Given
        List<String> content = Collections.emptyList();
        Pageable pageable = PageRequest.of(0, 10);
        Page<String> page = new PageImpl<>(content, pageable, 0);

        // When
        PageResponse<String> pageResponse = new PageResponse<>(page);

        // Then
        assertThat(pageResponse.getTotalElements()).isEqualTo(0L);
        assertThat(pageResponse.getTotalPages()).isEqualTo(1);
        assertThat(pageResponse.isEmpty()).isTrue();
        assertThat(pageResponse.isFirst()).isTrue();
        assertThat(pageResponse.isLast()).isTrue();
    }

    @Test
    void pageConstructor_WithLargeNumbers_ShouldHandleCorrectly() {
        // Given
        List<String> content = Arrays.asList("item1");
        Pageable pageable = PageRequest.of(999, 1);
        Page<String> page = new PageImpl<>(content, pageable, 1000000L);

        // When
        PageResponse<String> pageResponse = new PageResponse<>(page);

        // Then
        assertThat(pageResponse.getPageNumber()).isEqualTo(999);
        assertThat(pageResponse.getPageSize()).isEqualTo(1);
        assertThat(pageResponse.getTotalElements()).isEqualTo(1000000L);
        assertThat(pageResponse.getTotalPages()).isEqualTo(1000000);
    }
}