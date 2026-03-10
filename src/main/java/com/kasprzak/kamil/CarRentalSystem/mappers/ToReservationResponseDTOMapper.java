package com.kasprzak.kamil.CarRentalSystem.mappers;

import com.kasprzak.kamil.CarRentalSystem.dto.ReservationResponseDTO;
import com.kasprzak.kamil.CarRentalSystem.entity.Reservation;
import org.springframework.stereotype.Component;

@Component
public class ToReservationResponseDTOMapper {

    public ReservationResponseDTO map(final Reservation reservation){
        return ReservationResponseDTO
                .builder()
                .reservationId(reservation.getId())
                .carId(reservation.getCar().getId())
                .carType(reservation.getCar().getType())
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .status(reservation.getStatus())
                .clientName(reservation.getClientName())
                .build();
    }
}
