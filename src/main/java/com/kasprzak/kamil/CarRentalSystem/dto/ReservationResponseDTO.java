package com.kasprzak.kamil.CarRentalSystem.dto;

import com.kasprzak.kamil.CarRentalSystem.enums.CarType;
import com.kasprzak.kamil.CarRentalSystem.enums.ReservationStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReservationResponseDTO(
        Long reservationId,
        Long carId,
        CarType carType,
        ReservationStatus status,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String clientName
) {
}
