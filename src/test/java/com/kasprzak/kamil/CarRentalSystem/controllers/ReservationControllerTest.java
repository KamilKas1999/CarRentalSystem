package com.kasprzak.kamil.CarRentalSystem.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import com.kasprzak.kamil.CarRentalSystem.dto.ReservationRequestDTO;
import com.kasprzak.kamil.CarRentalSystem.dto.ReservationResponseDTO;
import com.kasprzak.kamil.CarRentalSystem.enums.CarType;
import com.kasprzak.kamil.CarRentalSystem.enums.ReservationStatus;
import com.kasprzak.kamil.CarRentalSystem.exceptions.ReservationNotFoundException;
import com.kasprzak.kamil.CarRentalSystem.services.CarRentalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.kasprzak.kamil.CarRentalSystem.exceptions.CarNotAvailableException;
import tools.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class ReservationControllerTest {

    @Mock
    private CarRentalService carRentalService;

    @InjectMocks
    private ReservationController reservationController;

    @Test
    void createReservation_shouldReturnReservationResponseDTO() {
        // given
        Long reservationId = 1L;
        Long carId = 1L;
        ReservationStatus newStatus = ReservationStatus.NEW;
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 1, 1);
        LocalDateTime stop = start.plusDays(3);
        ReservationRequestDTO request = new ReservationRequestDTO(CarType.SEDAN, start, 3, "name");
        var responseDTO = new ReservationResponseDTO(reservationId, carId, CarType.SEDAN, newStatus, start, stop, "name");

        when(carRentalService.reserveCar(request)).thenReturn(responseDTO);

        // when
        ReservationResponseDTO result = reservationController.createReservation(request);

        // then
        assertNotNull(result);
        assertEquals(1L, result.reservationId());
        assertEquals(CarType.SEDAN, result.carType());
        assertEquals(ReservationStatus.NEW, result.status());

        verify(carRentalService).reserveCar(request);
    }

    @Test
    void shouldReturnUpdatedReservation() {
        // given
        Long reservationId = 1L;
        Long carId = 1L;
        ReservationStatus newStatus = ReservationStatus.NEW;
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 1, 1);
        LocalDateTime stop = start.plusDays(3);
        var responseDTO = new ReservationResponseDTO(reservationId, carId, CarType.SEDAN, newStatus, start, stop, "name");

        when(carRentalService.updateReservationStatus(reservationId, newStatus)).thenReturn(responseDTO);

        // when
        ReservationResponseDTO result = reservationController.updateStatus(reservationId, newStatus);

        // then
        assertNotNull(result);
        assertEquals(reservationId, result.reservationId());
        assertEquals(CarType.SEDAN, result.carType());
        assertEquals(newStatus, result.status());

        verify(carRentalService).updateReservationStatus(reservationId, newStatus);
    }

    @Test
    void shouldReturnReservations() {
        // given
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 1, 1);
        LocalDateTime stop = start.plusDays(3);
        var res1 = new ReservationResponseDTO(1L, 11L, CarType.SEDAN, ReservationStatus.NEW, start, stop, "name");
        var res2 = new ReservationResponseDTO(2L, 22L, CarType.SUV, ReservationStatus.IN_PROGRESS, start, stop, "name");
        Page<ReservationResponseDTO> page = new PageImpl<>(List.of(res1, res2));
        when(carRentalService.getAllReservation(any(Pageable.class))).thenReturn(page);

        // when
        Page<ReservationResponseDTO> result = reservationController.getReservation(0, 10, "id");

        // then
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(CarType.SEDAN, result.getContent().getFirst().carType());

        // verify correct Pageable
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(carRentalService).getAllReservation(pageableCaptor.capture());
        Pageable pageableUsed = pageableCaptor.getValue();
        assertEquals(0, pageableUsed.getPageNumber());
        assertEquals(10, pageableUsed.getPageSize());
    }
}