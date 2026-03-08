package com.kasprzak.kamil.CarRentalSystem.controllers;


import com.kasprzak.kamil.CarRentalSystem.dto.CarDTO;
import com.kasprzak.kamil.CarRentalSystem.enums.CarType;
import com.kasprzak.kamil.CarRentalSystem.services.CarRentalService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CarController.class)
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CarRentalService carRentalService;

    @Test
    void shouldAddCarAndReturnCarDTO() throws Exception {
        // given
        CarDTO carDTO = CarDTO.builder()
                .id(1L)
                .type(CarType.SEDAN)
                .build();

        when(carRentalService.addCar(CarType.SEDAN)).thenReturn(carDTO);

        // when / then
        mockMvc.perform(post("/api/cars")
                        .param("type", "SEDAN")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.type").value("SEDAN"));

        verify(carRentalService).addCar(CarType.SEDAN);
    }

    @Test
    void shouldReturnTrueWhenCarIsAvailable() throws Exception {
        // given
        LocalDateTime start = LocalDateTime.of(2026, 3, 8, 10, 0);
        LocalDateTime end = start.plusDays(3);

        when(carRentalService.isCarAvailable(CarType.SEDAN, start, end)).thenReturn(true);

        // when & then
        mockMvc.perform(get("/api/cars/available")
                        .param("type", "SEDAN")
                        .param("start", "2026-03-08T10:00:00")
                        .param("numberOfDays", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value(true));

        verify(carRentalService).isCarAvailable(CarType.SEDAN, start, end);
    }

    @Test
    void shouldReturnFalseWhenCarIsNotAvailable() throws Exception {
        // given
        LocalDateTime start = LocalDateTime.of(2026, 3, 8, 10, 0);
        LocalDateTime end = start.plusDays(3);

        when(carRentalService.isCarAvailable(CarType.SEDAN, start, end)).thenReturn(false);

        // when & then
        mockMvc.perform(get("/api/cars/available")
                        .param("type", "SEDAN")
                        .param("start", "2026-03-08T10:00:00")
                        .param("numberOfDays", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value(false));

        verify(carRentalService).isCarAvailable(CarType.SEDAN, start, end);
    }
}