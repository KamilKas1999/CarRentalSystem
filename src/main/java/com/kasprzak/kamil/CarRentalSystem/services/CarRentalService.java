package com.kasprzak.kamil.CarRentalSystem.services;


import com.kasprzak.kamil.CarRentalSystem.dto.CarDTO;
import com.kasprzak.kamil.CarRentalSystem.dto.ReservationRequestDTO;
import com.kasprzak.kamil.CarRentalSystem.dto.ReservationResponseDTO;
import com.kasprzak.kamil.CarRentalSystem.enums.CarType;
import com.kasprzak.kamil.CarRentalSystem.enums.ReservationStatus;

import java.time.LocalDateTime;

public interface CarRentalService {

    CarDTO addCar(CarType carType);

    ReservationResponseDTO reserveCar(ReservationRequestDTO request);

    boolean isCarAvailable(CarType type, LocalDateTime start, LocalDateTime end);

    ReservationResponseDTO updateReservationStatus(Long reservationId, ReservationStatus newStatus);
}
