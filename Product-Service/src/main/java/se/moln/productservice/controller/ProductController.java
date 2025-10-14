package se.moln.productservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import se.moln.productservice.dto.*;
import se.moln.productservice.service.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final ProductService service;
    private final ProductImageAppService imageService;
    private final ProductReadService readService;
    private final ProductQueryService queryService;
    private final InventoryService inventoryService;

    public ProductController(ProductService service,
                             ProductImageAppService imageService,
                             ProductReadService readService,
                             ProductQueryService queryService,
                             InventoryService inventoryService) {
        this.service = service;
        this.imageService = imageService;
        this.readService = readService;
        this.queryService = queryService;
        this.inventoryService = inventoryService;
    }

    @Operation(summary = "Skapa en ny produkt", description = "Skapar en produkt baserat på inskickad JSON-body.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Produkt skapad",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "Ogiltig indata")
    })
    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest req) {
        log.info("Create product request received");
        log.debug("Create payload received");


        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req));
        //return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }



    @Operation(summary = "Lista produkter (paginering)", description = "Hämtar en paginerad lista av produkter. Styr resultatet med parametrarna page, size, sortBy och sortDir.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class)))
    })
    @GetMapping
    public ResponseEntity<PageResponse<ProductResponse>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        log.debug("Fetching products page={} size={} sortBy={} sortDir={}", page, size, sortBy, sortDir);

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProductResponse> products = service.getAllProducts(pageable);
        PageResponse<ProductResponse> response = new PageResponse<>(products);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Lista alla produkter", description = "Hämtar alla produkter utan paginering.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping("/all")
    public ResponseEntity<List<ProductResponse>> getAllProductsWithoutPagination() {
        List<ProductResponse> products = service.getAllProductsWithoutPagination();
        log.debug("Fetched all products count={}", products.size());
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Sök produkter", description = "Sökning på namn, kategori och/eller prisintervall. Alla parametrar är valfria.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice
    ) {
        List<ProductResponse> products = service.searchProducts(name, categoryName, minPrice, maxPrice);
        log.debug("Search completed name={} categoryName={} minPrice={} maxPrice={} count={}", name, categoryName, minPrice, maxPrice, products.size());
        return ResponseEntity.ok(products);
    }

    @Operation(
            summary = "Ladda upp produktbild",
            description = "Skicka som multipart/form-data med fältet 'file'. Bilden sparas lokalt och kopplas till produkten."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping(
            path = "/{id}/images",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ProductResponse> uploadImage(
            @Parameter(name = "id", description = "Produktens UUID", example = "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa3")
            @PathVariable UUID id,
            @Parameter(description = "Bildfil (jpg/png/webp)")
            @RequestPart("file") MultipartFile file
    ) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(imageService.uploadImage(id, file));
    }

    @Operation(summary = "Hämta produkt", description = "Hämtar en produkt via dess UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Produkten hittades inte")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(readService.getById(id));
    }

    @Operation(summary = "Lista aktiva produkter", description = "Hämtar en paginerad lista av aktiva produkter.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping("/active")
    public ResponseEntity<Page<ProductResponse>> listActive(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(queryService.listActive(page, size));
    }

    @Operation(summary = "Lista produkter per kategori", description = "Hämtar en paginerad lista av produkter filtrerade på kategori.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Kategorin hittades inte")
    })
    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<Page<ProductResponse>> listByCategory(
            @PathVariable UUID categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(queryService.listByCategory(categoryId, page, size));
    }


    @Operation(summary = "Uppdatera en befintlig produkt", description = "Uppdaterar alla ändringsbara fält för en produkt baserat på ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produkten uppdaterades framgångsrikt."),
            @ApiResponse(responseCode = "404", description = "Produkten med det angivna ID:t hittades inte.")
    })
    @PutMapping("{id}")
    public ResponseEntity<ProductResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody ProductRequest req){

        ProductResponse updateProduct = service.update(id, req);
        return ResponseEntity.ok(updateProduct);
    }



    @Operation(summary = "Ta bort en produkt", description = "Tar bort en produkt från databasen baserat på dess ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Produkten togs bort framgångsrikt."),
            @ApiResponse(responseCode = "404", description = "Produkten med det angivna ID:t hittades inte.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}