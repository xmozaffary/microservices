package se.moln.productservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.moln.productservice.dto.InventoryResponse;
import se.moln.productservice.dto.ProductResponse;
import se.moln.productservice.dto.PurchaseResponse;
import se.moln.productservice.exception.InsufficientStockException;
import se.moln.productservice.exception.ProductNotFoundException;
import se.moln.productservice.mappning.ProductMapper;
import se.moln.productservice.model.Product;
import se.moln.productservice.model.StockStatus;
import se.moln.productservice.repository.ProductRepository;

import java.util.UUID;

@Service
public class InventoryService {

    private final ProductRepository productRepo;
    private final ProductMapper mapper;


    public InventoryService(ProductRepository productRepo, ProductMapper mapper) {
        this.productRepo = productRepo;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public ProductResponse get(UUID productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));
        return mapper.toResponse(product);
    }

    @Transactional
    public PurchaseResponse purchase(UUID productId, int qty) {
        if (qty <= 0) throw new IllegalArgumentException("quantity must be > 0");

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + productId));

        try {
            product.reserveStock(qty); // kastar InsufficientStockException vid behov
        } catch (InsufficientStockException e) {
            // Låt felet bubbla – handlern gör ProblemDetail (409)
            throw e;
        } catch (IllegalStateException | IndexOutOfBoundsException e) {
            // Defensivt fallback ifall någon gammal kod kastar andra exceptions
            throw new InsufficientStockException(product.getId(), qty, product.getStockQuantity(),
                    e.getMessage() != null ? e.getMessage() : "Otillräckligt lager");
        }

        productRepo.save(product);
        ProductResponse productDto = mapper.toResponse(product);

        return new PurchaseResponse(
                productDto,
                qty,
                product.getStockQuantity(),
                "Purchase successful."
        );
    }


    @Transactional
    public InventoryResponse refund(UUID productId, int qty) {
        if (qty <= 0) throw new IllegalArgumentException("quantity must be > 0");

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + productId));

        product.releaseStock(qty); // ökar lagersaldot
        productRepo.save(product);

        return new InventoryResponse(
                product.getId(),
                product.getStockQuantity(),
                "Return successful."
        );
    }
}