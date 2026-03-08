package com.kasprzak.kamil.CarRentalSystem.dto;

import com.kasprzak.kamil.CarRentalSystem.enums.CarType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReservationRequestDTO(
        CarType carType,
        LocalDateTime startDate,
        int numberOfDays
) {
}
