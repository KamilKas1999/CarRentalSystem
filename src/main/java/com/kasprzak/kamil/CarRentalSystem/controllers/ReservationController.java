package com.kasprzak.kamil.CarRentalSystem.controllers;

import com.kasprzak.kamil.CarRentalSystem.dto.ReservationRequestDTO;
import com.kasprzak.kamil.CarRentalSystem.dto.ReservationResponseDTO;
import com.kasprzak.kamil.CarRentalSystem.enums.ReservationStatus;
import com.kasprzak.kamil.CarRentalSystem.services.CarRentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final CarRentalService carRentalService;

    @PostMapping
    public ReservationResponseDTO createReservation(@RequestBody ReservationRequestDTO request) {
        return carRentalService.reserveCar(request);
    }

    @PatchMapping("/{reservationId}/status")
    public ReservationResponseDTO updateStatus(
            @PathVariable Long reservationId,
            @RequestParam ReservationStatus newStatus
    ) {
        return carRentalService.updateReservationStatus(reservationId, newStatus);
    }

}
