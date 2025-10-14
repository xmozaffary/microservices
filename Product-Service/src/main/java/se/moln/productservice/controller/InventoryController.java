package se.moln.productservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.moln.productservice.dto.AdjustStockRequest;
import se.moln.productservice.dto.InventoryResponse;
import se.moln.productservice.dto.PurchaseResponse;
import se.moln.productservice.service.InventoryService;

import java.util.UUID;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PurchaseResponse.class))),
            @ApiResponse(responseCode = "409", description = "Otillräckligt lager",
                    content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "400", description = "Ogiltig indata",
                    content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "404", description = "Produkten hittades inte",
                    content = @Content(mediaType = "application/problem+json"))
    })
    @PostMapping("/{productId}/purchase")
    public ResponseEntity<PurchaseResponse> purchase(
            @Parameter(name = "productId", description = "Produktens UUID", example = "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa3")
            @PathVariable UUID productId,
            @RequestBody @Valid AdjustStockRequest req
    ) {
        return ResponseEntity.ok(service.purchase(productId, req.quantity()));
    }

    @Operation(summary = "Retur till lager", description = "Ökar lagersaldot (återlägger) med angivet antal.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InventoryResponse.class))),
            @ApiResponse(responseCode = "400", description = "Ogiltig indata",
                    content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "404", description = "Produkten hittades inte",
                    content = @Content(mediaType = "application/problem+json"))
    })
    @PostMapping("/{productId}/return")
    public ResponseEntity<InventoryResponse> doReturn(
            @Parameter(name = "productId", description = "Produktens UUID", example = "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa3")
            @PathVariable UUID productId,
            @RequestBody @Valid AdjustStockRequest req
    ) {
        return ResponseEntity.ok(service.refund(productId, req.quantity()));
    }
}