package com.kasprzak.kamil.CarRentalSystem.dto;

import com.kasprzak.kamil.CarRentalSystem.enums.CarType;
import com.kasprzak.kamil.CarRentalSystem.enums.ReservationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReservationResponseDTO(
        @NotNull Long reservationId,
        @NotNull Long carId,
        @NotNull CarType carType,
        @NotNull ReservationStatus status,
        @NotNull LocalDateTime startDate,
        @NotNull LocalDateTime endDate
) {
}
