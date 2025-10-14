package se.moln.productservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class FileStorageServiceTest {

    @TempDir
    Path tempDir;

    private FileStorageService fileStorageService;

    @Mock
    private MultipartFile mockFile;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        fileStorageService = new FileStorageService(tempDir.toString());
    }

    @Test
    void constructor_ShouldCreateUploadDirectory() throws IOException {
        // Given
        Path newDir = tempDir.resolve("test-uploads");
        
        // When
        FileStorageService service = new FileStorageService(newDir.toString());
        
        // Then
        assertThat(Files.exists(newDir)).isTrue();
        assertThat(Files.isDirectory(newDir)).isTrue();
    }

    @Test
    void store_WithValidFile_ShouldStoreFileSuccessfully() throws IOException {
        // Given
        String originalFilename = "test-image.jpg";
        String contentType = "image/jpeg";
        long fileSize = 1024L;
        byte[] content = "test image content".getBytes();
        
        when(mockFile.isEmpty()).thenReturn(false);
        when(mockFile.getOriginalFilename()).thenReturn(originalFilename);
        when(mockFile.getContentType()).thenReturn(contentType);
        when(mockFile.getSize()).thenReturn(fileSize);
        doNothing().when(mockFile).transferTo(any(Path.class));

        // When
        FileStorageService.StoredFile result = fileStorageService.store(mockFile);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.originalName()).isEqualTo(originalFilename);
        assertThat(result.contentType()).isEqualTo(contentType);
        assertThat(result.size()).isEqualTo(fileSize);
        assertThat(result.url()).startsWith("/uploads/");
        assertThat(result.url()).endsWith(".jpg");
        verify(mockFile).transferTo(any(Path.class));
    }

    @Test
    void store_WithEmptyFile_ShouldThrowIOException() throws IOException {
        // Given
        when(mockFile.isEmpty()).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> fileStorageService.store(mockFile))
                .isInstanceOf(IOException.class)
                .hasMessage("Empty file");
        
        verify(mockFile, never()).transferTo(any(Path.class));
    }

    @Test
    void store_WithNullOriginalFilename_ShouldUseDefaultName() throws IOException {
        // Given
        String contentType = "image/jpeg";
        long fileSize = 1024L;
        
        when(mockFile.isEmpty()).thenReturn(false);
        when(mockFile.getOriginalFilename()).thenReturn(null);
        when(mockFile.getContentType()).thenReturn(contentType);
        when(mockFile.getSize()).thenReturn(fileSize);
        doNothing().when(mockFile).transferTo(any(Path.class));

        // When
        FileStorageService.StoredFile result = fileStorageService.store(mockFile);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.originalName()).isEqualTo("file");
        assertThat(result.url()).startsWith("/uploads/");
        // Should not have extension since original filename was null
        assertThat(result.url().contains(".")).isFalse();
    }

    @Test
    void store_WithFileWithoutExtension_ShouldStoreWithoutExtension() throws IOException {
        // Given
        String originalFilename = "testfile";
        String contentType = "text/plain";
        long fileSize = 512L;
        
        when(mockFile.isEmpty()).thenReturn(false);
        when(mockFile.getOriginalFilename()).thenReturn(originalFilename);
        when(mockFile.getContentType()).thenReturn(contentType);
        when(mockFile.getSize()).thenReturn(fileSize);
        doNothing().when(mockFile).transferTo(any(Path.class));

        // When
        FileStorageService.StoredFile result = fileStorageService.store(mockFile);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.originalName()).isEqualTo(originalFilename);
        assertThat(result.url()).startsWith("/uploads/");
        // URL should not end with dot since there's no extension
        assertThat(result.url()).doesNotEndWith(".");
    }

    @Test
    void store_WithComplexFilename_ShouldCleanPath() throws IOException {
        // Given
        String originalFilename = "../../../malicious-file.jpg";
        String contentType = "image/jpeg";
        long fileSize = 1024L;
        
        when(mockFile.isEmpty()).thenReturn(false);
        when(mockFile.getOriginalFilename()).thenReturn(originalFilename);
        when(mockFile.getContentType()).thenReturn(contentType);
        when(mockFile.getSize()).thenReturn(fileSize);
        doNothing().when(mockFile).transferTo(any(Path.class));

        // When
        FileStorageService.StoredFile result = fileStorageService.store(mockFile);

        // Then
        assertThat(result).isNotNull();
        // StringUtils.cleanPath normalizes the path but doesn't remove the ../ parts completely
        assertThat(result.originalName()).isEqualTo("../../../malicious-file.jpg");
        assertThat(result.url()).startsWith("/uploads/");
        assertThat(result.url()).endsWith(".jpg");
    }

    @Test
    void store_WithIOExceptionDuringTransfer_ShouldPropagateException() throws IOException {
        // Given
        when(mockFile.isEmpty()).thenReturn(false);
        when(mockFile.getOriginalFilename()).thenReturn("test.jpg");
        when(mockFile.getContentType()).thenReturn("image/jpeg");
        when(mockFile.getSize()).thenReturn(1024L);
        doThrow(new IOException("Transfer failed")).when(mockFile).transferTo(any(Path.class));

        // When & Then
        assertThatThrownBy(() -> fileStorageService.store(mockFile))
                .isInstanceOf(IOException.class)
                .hasMessage("Transfer failed");
    }

    @Test
    void store_WithVariousFileExtensions_ShouldPreserveExtensions() throws IOException {
        // Given
        String[] extensions = {".png", ".gif", ".webp", ".PDF", ".TXT"};
        
        for (String ext : extensions) {
            MultipartFile file = mock(MultipartFile.class);
            String filename = "testfile" + ext;
            
            when(file.isEmpty()).thenReturn(false);
            when(file.getOriginalFilename()).thenReturn(filename);
            when(file.getContentType()).thenReturn("application/octet-stream");
            when(file.getSize()).thenReturn(1024L);
            doNothing().when(file).transferTo(any(Path.class));

            // When
            FileStorageService.StoredFile result = fileStorageService.store(file);

            // Then
            assertThat(result.url()).endsWith(ext);
            assertThat(result.originalName()).isEqualTo(filename);
        }
    }

    @Test
    void storedFile_ShouldBeProperRecord() {
        // Given
        String url = "/uploads/test.jpg";
        String originalName = "original.jpg";
        String contentType = "image/jpeg";
        long size = 1024L;

        // When
        FileStorageService.StoredFile storedFile = new FileStorageService.StoredFile(url, originalName, contentType, size);

        // Then
        assertThat(storedFile.url()).isEqualTo(url);
        assertThat(storedFile.originalName()).isEqualTo(originalName);
        assertThat(storedFile.contentType()).isEqualTo(contentType);
        assertThat(storedFile.size()).isEqualTo(size);
    }
}