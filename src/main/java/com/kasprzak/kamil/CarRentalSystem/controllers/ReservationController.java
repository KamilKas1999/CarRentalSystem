package com.kasprzak.kamil.CarRentalSystem.controllers;

import com.kasprzak.kamil.CarRentalSystem.dto.CarDTO;
import com.kasprzak.kamil.CarRentalSystem.dto.ReservationRequestDTO;
import com.kasprzak.kamil.CarRentalSystem.dto.ReservationResponseDTO;
import com.kasprzak.kamil.CarRentalSystem.enums.ReservationStatus;
import com.kasprzak.kamil.CarRentalSystem.services.CarRentalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final CarRentalService carRentalService;

    @PostMapping
    public ReservationResponseDTO createReservation(@Valid @RequestBody ReservationRequestDTO request) {
        return carRentalService.reserveCar(request);
    }

    @PatchMapping("/{reservationId}/status")
    public ReservationResponseDTO updateStatus(
            @PathVariable Long reservationId,
            @RequestParam ReservationStatus newStatus
    ) {
        return carRentalService.updateReservationStatus(reservationId, newStatus);
    }

    @GetMapping
    public Page<ReservationResponseDTO> getReservation(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return carRentalService.getAllReservation(pageable);
    }

}
