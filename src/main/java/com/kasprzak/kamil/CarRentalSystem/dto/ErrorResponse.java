package com.kasprzak.kamil.CarRentalSystem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorResponse(
        int status,
        String error,
        String message,
        LocalDateTime timestamp
){
}
