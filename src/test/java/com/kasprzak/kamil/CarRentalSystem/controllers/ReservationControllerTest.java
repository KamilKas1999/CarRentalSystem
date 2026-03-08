package com.kasprzak.kamil.CarRentalSystem.controllers;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.kasprzak.kamil.CarRentalSystem.exceptions.CarNotAvailableException;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CarRentalService carRentalService;

    @Test
    void shouldCreateReservationAndReturnReservationResponseDTO() throws Exception {
        // given
        var request = ReservationRequestDTO.builder()
                .carType(CarType.SEDAN)
                .startDate(LocalDateTime.of(2026, 3, 8, 10, 0))
                .numberOfDays(3)
                .build();

        var response = ReservationResponseDTO.builder()
                .reservationId(1L)
                .carId(10L)
                .carType(CarType.SEDAN)
                .startDate(LocalDateTime.of(2026, 3, 8, 10, 0))
                .endDate(LocalDateTime.of(2026, 3, 11, 10, 0))
                .build();

        when(carRentalService.reserveCar(any(ReservationRequestDTO.class))).thenReturn(response);

        // when & then
        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationId").value(1L))
                .andExpect(jsonPath("$.carId").value(10L))
                .andExpect(jsonPath("$.carType").value("SEDAN"))
                .andExpect(jsonPath("$.startDate").exists())
                .andExpect(jsonPath("$.endDate").exists());

        verify(carRentalService).reserveCar(any(ReservationRequestDTO.class));
    }

    @Test
    void shouldReturnConflictWhenCarIsNotAvailable() throws Exception {
        // given
        ReservationRequestDTO request = ReservationRequestDTO.builder()
                .carType(CarType.SEDAN)
                .startDate(LocalDateTime.of(2026, 3, 8, 10, 0))
                .numberOfDays(3)
                .build();

        when(carRentalService.reserveCar(any(ReservationRequestDTO.class)))
                .thenThrow(new CarNotAvailableException("Car of type SEDAN is not available"));

        // when & then
        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Car of type SEDAN is not available"));
    }

    @Test
    void shouldUpdateReservationStatusAndReturnReservationResponseDTO() throws Exception {
        // given
        var response = ReservationResponseDTO.builder()
                .reservationId(1L)
                .carId(10L)
                .carType(CarType.SEDAN)
                .startDate(LocalDateTime.of(2026, 3, 8, 10, 0))
                .endDate(LocalDateTime.of(2026, 3, 11, 10, 0))
                .status(ReservationStatus.CANCELLED)
                .build();

        when(carRentalService.updateReservationStatus(eq(1L), eq(ReservationStatus.CANCELLED)))
                .thenReturn(response);

        // when & then
        mockMvc.perform(patch("/api/reservations/1/status")
                        .param("newStatus", "CANCELLED")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationId").value(1L))
                .andExpect(jsonPath("$.status").value("CANCELLED"));

        verify(carRentalService).updateReservationStatus(eq(1L), eq(ReservationStatus.CANCELLED));
    }

    @Test
    void shouldReturnNotFoundWhenReservationDoesNotExist() throws Exception {
        // given
        when(carRentalService.updateReservationStatus(eq(99L), eq(ReservationStatus.CANCELLED)))
                .thenThrow(new ReservationNotFoundException("Reservation with ID 99 not found"));

        // when & then
        mockMvc.perform(patch("/api/reservations/99/status")
                        .param("newStatus", "CANCELLED")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Reservation with ID 99 not found"));
    }

}