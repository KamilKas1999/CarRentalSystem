package com.kasprzak.kamil.CarRentalSystem.mappers;

import com.kasprzak.kamil.CarRentalSystem.dto.CarDTO;
import com.kasprzak.kamil.CarRentalSystem.entity.Car;
import com.kasprzak.kamil.CarRentalSystem.enums.CarStatus;
import com.kasprzak.kamil.CarRentalSystem.enums.CarType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ToCarDTOMapperTest {

    private final ToCarDTOMapper mapper = new ToCarDTOMapper();

    @Test
    void shouldMapCarToCarDTO() {
        // given
        var car = Car.builder()
                .id(1L)
                .type(CarType.SUV)
                .status(CarStatus.ACTIVE)
                .registration("123")
                .build();

        // when
        CarDTO result = mapper.map(car);

        // then
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals(CarType.SUV, result.type());
        assertEquals(CarStatus.ACTIVE, result.status());
        assertEquals("123", result.registration());
    }
}