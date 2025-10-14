package se.moln.productservice.dto;


public record PurchaseResponse(
        ProductResponse product,
        int purchasedQuantity,
        int newStockQuantity,
        String message
) {}