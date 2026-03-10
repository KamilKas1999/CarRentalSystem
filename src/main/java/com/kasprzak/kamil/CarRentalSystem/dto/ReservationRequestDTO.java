package com.kasprzak.kamil.CarRentalSystem.dto;

import com.kasprzak.kamil.CarRentalSystem.enums.CarType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReservationRequestDTO(
        @NotNull CarType carType,
        @NotNull LocalDateTime startDate,
        @NotNull @Positive int numberOfDays,
        @NotEmpty String clientName
) {
}
