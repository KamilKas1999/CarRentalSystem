package com.kasprzak.kamil.CarRentalSystem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorResponse(
        @NotNull int status,
        @NotNull String error,
        @NotNull String message,
        @NotNull LocalDateTime timestamp
){
}
