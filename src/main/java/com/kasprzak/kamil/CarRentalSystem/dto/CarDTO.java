package com.kasprzak.kamil.CarRentalSystem.dto;

import com.kasprzak.kamil.CarRentalSystem.enums.CarStatus;
import com.kasprzak.kamil.CarRentalSystem.enums.CarType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CarDTO(
        @NotNull Long id,
        @NotNull CarType type,
        @NotNull CarStatus status,
        @NotEmpty String registration
) {
}
