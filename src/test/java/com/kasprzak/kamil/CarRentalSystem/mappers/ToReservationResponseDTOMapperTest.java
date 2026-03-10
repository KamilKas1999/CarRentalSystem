package com.kasprzak.kamil.CarRentalSystem.mappers;

import com.kasprzak.kamil.CarRentalSystem.dto.ReservationResponseDTO;
import com.kasprzak.kamil.CarRentalSystem.entity.Car;
import com.kasprzak.kamil.CarRentalSystem.entity.Reservation;
import com.kasprzak.kamil.CarRentalSystem.enums.CarType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ToReservationResponseDTOMapperTest {

    private ToReservationResponseDTOMapper mapper = new ToReservationResponseDTOMapper();

    @Test
    void shouldMapReservationToReservationResponseDTO() {
        // given
        var car = Car.builder()
                .id(1L)
                .type(CarType.SEDAN)
                .build();

        var reservation = Reservation.builder()
                .id(10L)
                .car(car)
                .startDate(LocalDateTime.of(2026, 3, 8, 10, 0))
                .endDate(LocalDateTime.of(2026, 3, 11, 10, 0))
                .clientName("name")
                .build();

        // when
        ReservationResponseDTO result = mapper.map(reservation);

        // then
        assertNotNull(result);
        assertEquals(10L, result.reservationId());
        assertEquals(1L, result.carId());
        assertEquals(CarType.SEDAN, result.carType());
        assertEquals("name", result.clientName());
        assertEquals(LocalDateTime.of(2026, 3, 8, 10, 0), result.startDate());
        assertEquals(LocalDateTime.of(2026, 3, 11, 10, 0), result.endDate());
    }
}