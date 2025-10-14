package se.moln.productservice.dto;

import java.util.UUID;

public record InventoryResponse(
        UUID productId,
        int quantity,
        String message
) {}
