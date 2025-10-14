package se.moln.orderservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MovieSearchRequest(
        @NotBlank(message = "Movie title is required")
        @Size(min = 2, max = 100, message = "Title must be between 2 and 100 characters")
        String title
) {
}
