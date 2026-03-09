package com.kasprzak.kamil.CarRentalSystem.dto;

import jakarta.validation.constraints.NotNull;

public record AvailabilityResponseDTO(
        @NotNull boolean available
) {
}
