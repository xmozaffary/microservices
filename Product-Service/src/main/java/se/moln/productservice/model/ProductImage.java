package se.moln.productservice.model;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Embeddable
public class ProductImage {
    @NotBlank
    @Column(nullable = false, length = 1000)
    private String url;

    @Size(max = 255)
    private String fileName;

    @Size(max = 100)
    private String contentType;

    private Long sizeBytes;

    protected ProductImage() {} // JPA

    public ProductImage(String url, String fileName, String contentType, Long sizeBytes) {
        this.url = url;
        this.fileName = fileName;
        this.contentType = contentType;
        this.sizeBytes = sizeBytes;
    }

    public String getUrl() { return url; }
    public String getFileName() { return fileName; }
    public String getContentType() { return contentType; }
    public Long getSizeBytes() { return sizeBytes; }
}