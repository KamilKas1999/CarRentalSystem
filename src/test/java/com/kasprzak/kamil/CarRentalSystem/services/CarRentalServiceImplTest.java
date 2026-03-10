package com.kasprzak.kamil.CarRentalSystem.services;

import com.kasprzak.kamil.CarRentalSystem.dto.ReservationRequestDTO;
import com.kasprzak.kamil.CarRentalSystem.dto.ReservationResponseDTO;
import com.kasprzak.kamil.CarRentalSystem.entity.Car;
import com.kasprzak.kamil.CarRentalSystem.entity.Reservation;
import com.kasprzak.kamil.CarRentalSystem.enums.CarStatus;
import com.kasprzak.kamil.CarRentalSystem.enums.CarType;
import com.kasprzak.kamil.CarRentalSystem.enums.ReservationStatus;
import com.kasprzak.kamil.CarRentalSystem.exceptions.CarNotAvailableException;
import com.kasprzak.kamil.CarRentalSystem.exceptions.ReservationNotFoundException;
import com.kasprzak.kamil.CarRentalSystem.mappers.ToReservationResponseDTOMapper;
import com.kasprzak.kamil.CarRentalSystem.repositories.CarRepository;
import com.kasprzak.kamil.CarRentalSystem.repositories.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarRentalServiceImplTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ToReservationResponseDTOMapper mapper;

    @InjectMocks
    private CarRentalServiceImpl carRentalService;

    @Test
    public void shouldReturnFalseForIsCarAvailable(){
        //given
        var type = CarType.SEDAN;
        var start = LocalDateTime.of(2026, 3, 8, 10, 0);
        var end = start.plusDays(3);
        when(carRepository.findFirstAvailableCar(eq(CarType.SEDAN), eq(CarStatus.ACTIVE), eq(start), eq(end), anyList())).thenReturn(Optional.empty());

        // when
        boolean result = carRentalService.isCarAvailable(type, start, end);

        // then
        assertFalse(result);
        verify(carRepository).findFirstAvailableCar(
                eq(CarType.SEDAN),
                eq(CarStatus.ACTIVE),
                eq(start),
                eq(end),
                anyList()
        );

    }

    @Test
    public void shouldReturnTrueForIsCarAvailable(){
        //given
        var type = CarType.SEDAN;
        var start = LocalDateTime.of(2026, 3, 8, 10, 0);
        var end = start.plusDays(3);
        when(carRepository.findFirstAvailableCar(eq(CarType.SEDAN), eq(CarStatus.ACTIVE), eq(start), eq(end), anyList())).thenReturn(Optional.of(mock(Car.class)));

        // when
        boolean result = carRentalService.isCarAvailable(type, start, end);

        // then
        assertTrue(result);
        verify(carRepository).findFirstAvailableCar(
                eq(CarType.SEDAN),
                eq(CarStatus.ACTIVE),
                eq(start),
                eq(end),
                anyList()
        );

    }

    @Test
    void shouldReserveCarSuccessfully() {
        // given
        var start = LocalDateTime.of(2026, 3, 8, 10, 0);
        var end = start.plusDays(3);
        var request = new ReservationRequestDTO(CarType.SEDAN, start, 3, "name");

        when(carRepository.findFirstAvailableCar(eq(CarType.SEDAN), eq(CarStatus.ACTIVE), eq(start), eq(end), anyList()))
                .thenReturn(Optional.of(mock(Car.class)));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(mapper.map(any(Reservation.class))).thenReturn(mock(ReservationResponseDTO.class));

        // when
        ReservationResponseDTO response = carRentalService.reserveCar(request);

        // then
        assertNotNull(response);
        verify(carRepository).findFirstAvailableCar(eq(CarType.SEDAN), eq(CarStatus.ACTIVE), eq(start), eq(end), anyList());
        verify(reservationRepository).save(any(Reservation.class));
        verify(mapper).map(any(Reservation.class));
    }

    @Test
    void shouldThrowWhenNoCarAvailable() {
        // given
        var start = LocalDateTime.of(2026, 3, 8, 10, 0);
        var end = start.plusDays(3);
        var request = new ReservationRequestDTO(CarType.SEDAN, start, 3, "name");

        when(carRepository.findFirstAvailableCar(eq(CarType.SEDAN), eq(CarStatus.ACTIVE), eq(start), eq(end), anyList()))
                .thenReturn(Optional.empty());

        // when / then
        CarNotAvailableException ex = assertThrows(CarNotAvailableException.class,
                () -> carRentalService.reserveCar(request));
        assertTrue(ex.getMessage().contains("Car type SEDAN not available"));
    }

    @Test
    void shouldUpdateReservationStatusSuccessfully() {
        // given
        var start = LocalDateTime.of(2026, 3, 8, 10, 0);
        var end = start.plusDays(3);

        var reservation = Reservation.builder()
                .id(1L)
                .status(ReservationStatus.NEW)
                .car(mock(Car.class))
                .startDate(start)
                .endDate(end)
                .build();

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(mapper.map(any(Reservation.class))).thenReturn(mock(ReservationResponseDTO.class));

        // when
        ReservationResponseDTO response = carRentalService.updateReservationStatus(1L, ReservationStatus.IN_PROGRESS);

        // then
        assertNotNull(response);
        assertEquals(ReservationStatus.IN_PROGRESS, reservation.getStatus());
        verify(reservationRepository).save(reservation);
        verify(mapper).map(reservation);
    }

    @Test
    void shouldThrowForInvalidStatusTransition() {
        // given
        Reservation reservation = Reservation.builder()
                .id(1L)
                .status(ReservationStatus.NEW)
                .build();
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        // when / then
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> carRentalService.updateReservationStatus(1L, ReservationStatus.RETURNED));
        assertTrue(ex.getMessage().contains("Invalid status transition"));
    }

    @Test
    void shouldThrowWhenReservationNotFound() {
        // given
        when(reservationRepository.findById(999L)).thenReturn(Optional.empty());

        // when / then
        ReservationNotFoundException ex = assertThrows(ReservationNotFoundException.class,
                () -> carRentalService.updateReservationStatus(999L, ReservationStatus.IN_PROGRESS));
        assertTrue(ex.getMessage().contains("Reservation with id 999 not found"));
    }

    @Test
    void findAvailableCarShouldReturnCar() {
        // given
        var start = LocalDateTime.of(2026, 3, 8, 10, 0);
        var end = start.plusDays(3);
        var request = new ReservationRequestDTO(CarType.SEDAN, start, 3, "name");


        when(carRepository.findFirstAvailableCar(eq(CarType.SEDAN), eq(CarStatus.ACTIVE), eq(start), eq(end), anyList()))
                .thenReturn(Optional.of(mock(Car.class)));
        when(reservationRepository.save(any())).thenReturn(null);
        when(mapper.map(any())).thenReturn(mock(ReservationResponseDTO.class));

        // when
        ReservationResponseDTO response = carRentalService.reserveCar(request);

        // then
        assertNotNull(response);
        verify(carRepository).findFirstAvailableCar(eq(CarType.SEDAN), eq(CarStatus.ACTIVE), eq(start), eq(end), anyList());
    }

    @Test
    void findAvailableCarShouldThrowWhenNoCar() {
        // given
        var start = LocalDateTime.of(2026, 3, 8, 10, 0);
        var end = start.plusDays(3);
        var request = new ReservationRequestDTO(CarType.SEDAN, start, 3, "name");
        when(carRepository.findFirstAvailableCar(eq(CarType.SEDAN), eq(CarStatus.ACTIVE), eq(start), eq(end), anyList()))
                .thenReturn(Optional.empty());

        // when / then
        assertThrows(CarNotAvailableException.class, () -> carRentalService.reserveCar(request));
    }
}