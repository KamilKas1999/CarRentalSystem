package com.kasprzak.kamil.CarRentalSystem.dto;

import com.kasprzak.kamil.CarRentalSystem.enums.CarType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AvailabilityRequestDTO(
        @NotNull CarType carType,
        @NotNull LocalDateTime startDate,
        @NotNull int numberOfDays
) {
}
