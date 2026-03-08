package com.kasprzak.kamil.CarRentalSystem.dto;

import com.kasprzak.kamil.CarRentalSystem.enums.CarType;

import java.time.LocalDateTime;

public record AvailabilityRequestDTO(
        CarType carType,
        LocalDateTime startDate,
        int numberOfDays
) {
}
