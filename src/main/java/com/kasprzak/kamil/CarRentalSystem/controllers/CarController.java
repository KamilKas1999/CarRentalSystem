package com.kasprzak.kamil.CarRentalSystem.controllers;

import com.kasprzak.kamil.CarRentalSystem.dto.AvailabilityResponseDTO;
import com.kasprzak.kamil.CarRentalSystem.dto.CarDTO;
import com.kasprzak.kamil.CarRentalSystem.enums.CarType;
import com.kasprzak.kamil.CarRentalSystem.services.CarRentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarRentalService carRentalService;

    @PostMapping
    public CarDTO addCar(@RequestParam CarType type) {
        return carRentalService.addCar(type);
    }

    @GetMapping("/available")
    public AvailabilityResponseDTO checkAvailableByType(
            @RequestParam CarType type,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam int numberOfDays
    ) {
        LocalDateTime end = start.plusDays(numberOfDays);
        boolean available = carRentalService.isCarAvailable(type, start, end);
        return new AvailabilityResponseDTO(available);
    }
}
