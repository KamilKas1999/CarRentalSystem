package com.kasprzak.kamil.CarRentalSystem.dto;

import com.kasprzak.kamil.CarRentalSystem.enums.CarType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReservationRequestDTO(
        @NotNull CarType carType,
        @NotNull LocalDateTime startDate,
        @NotNull int numberOfDays
) {
}
