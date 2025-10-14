package se.moln.productservice.dto;

import jakarta.validation.constraints.Min;

public record AdjustStockRequest(
        @Min(value = 1, message = "quantity måste vara minst 1")
        int quantity
) {}
