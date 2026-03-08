package com.kasprzak.kamil.CarRentalSystem.dto;

import com.kasprzak.kamil.CarRentalSystem.enums.CarStatus;
import com.kasprzak.kamil.CarRentalSystem.enums.CarType;
import lombok.Builder;

@Builder
public record CarDTO(
        Long id,
        CarType type,
        CarStatus status
) {
}
