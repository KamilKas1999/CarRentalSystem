package com.kasprzak.kamil.CarRentalSystem.mappers;

import com.kasprzak.kamil.CarRentalSystem.dto.CarDTO;
import com.kasprzak.kamil.CarRentalSystem.dto.ReservationResponseDTO;
import com.kasprzak.kamil.CarRentalSystem.entity.Car;
import com.kasprzak.kamil.CarRentalSystem.entity.Reservation;
import org.springframework.stereotype.Component;

@Component
public class ToCarDTOMapper {

    public CarDTO map(final Car car){
        return CarDTO
                .builder()
                .id(car.getId())
                .type(car.getType())
                .status(car.getStatus())
                .registration(car.getRegistration())
                .build();
    }
}
