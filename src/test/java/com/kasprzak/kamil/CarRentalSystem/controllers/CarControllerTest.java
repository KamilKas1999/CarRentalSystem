package com.kasprzak.kamil.CarRentalSystem.controllers;


import com.kasprzak.kamil.CarRentalSystem.dto.AvailabilityResponseDTO;
import com.kasprzak.kamil.CarRentalSystem.dto.CarDTO;
import com.kasprzak.kamil.CarRentalSystem.enums.CarStatus;
import com.kasprzak.kamil.CarRentalSystem.enums.CarType;
import com.kasprzak.kamil.CarRentalSystem.services.CarRentalService;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarControllerTest {

    @Mock
    private CarRentalService carRentalService;

    @InjectMocks
    private CarController carController;

    @Test
    void shouldReturnCarDTO() {
        // given
        var carDTO = CarDTO.builder()
                .id(1L)
                .type(CarType.SEDAN)
                .registration("123")
                .build();

        when(carRentalService.addCar(CarType.SEDAN, "123")).thenReturn(carDTO);

        // when
        CarDTO result = carController.addCar(CarType.SEDAN, "123");

        // then
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals(CarType.SEDAN, result.type());
        verify(carRentalService).addCar(CarType.SEDAN, "123");
    }

    @Test
    void shouldReturnTrue() {
        // given
        var start = LocalDateTime.of(2026, 3, 8, 10, 0);
        var numberOfDays = 3;
        var end = start.plusDays(numberOfDays);

        when(carRentalService.isCarAvailable(CarType.SEDAN, start, end)).thenReturn(true);

        // when
        AvailabilityResponseDTO response = carController.checkAvailableByType(CarType.SEDAN, start, numberOfDays);

        // then
        assertNotNull(response);
        assertTrue(response.available());
        verify(carRentalService).isCarAvailable(CarType.SEDAN, start, end);
    }

    @Test
    void shouldReturnFalse() {
        // given
        var start = LocalDateTime.of(2026, 3, 8, 10, 0);
        var numberOfDays = 3;
        var end = start.plusDays(numberOfDays);

        when(carRentalService.isCarAvailable(CarType.SEDAN, start, end)).thenReturn(false);

        // when
        AvailabilityResponseDTO response = carController.checkAvailableByType(CarType.SEDAN, start, numberOfDays);

        // then
        assertNotNull(response);
        assertFalse(response.available());
        verify(carRentalService).isCarAvailable(CarType.SEDAN, start, end);
    }

    @Test
    void shouldReturnCars() {
        // given
        CarDTO car1 = new CarDTO(1L, CarType.SEDAN, CarStatus.ACTIVE, "123");
        CarDTO car2 = new CarDTO(2L, CarType.VAN, CarStatus.ACTIVE, "123");
        Page<CarDTO> page = new PageImpl<>(List.of(car1, car2));
        when(carRentalService.getAllCars(any(Pageable.class))).thenReturn(page);

        // when
        Page<CarDTO> result = carController.getCars(0, 10, "id");

        // then
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(CarType.SEDAN, result.getContent().getFirst().type());
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(carRentalService).getAllCars(pageableCaptor.capture());
        Pageable pageableUsed = pageableCaptor.getValue();
        assertEquals(0, pageableUsed.getPageNumber());
        assertEquals(10, pageableUsed.getPageSize());
    }
}