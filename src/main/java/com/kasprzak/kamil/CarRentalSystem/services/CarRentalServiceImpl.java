package com.kasprzak.kamil.CarRentalSystem.services;

import com.kasprzak.kamil.CarRentalSystem.dto.CarDTO;
import com.kasprzak.kamil.CarRentalSystem.dto.ReservationRequestDTO;
import com.kasprzak.kamil.CarRentalSystem.dto.ReservationResponseDTO;
import com.kasprzak.kamil.CarRentalSystem.entity.Car;
import com.kasprzak.kamil.CarRentalSystem.entity.Reservation;
import com.kasprzak.kamil.CarRentalSystem.enums.CarStatus;
import com.kasprzak.kamil.CarRentalSystem.enums.CarType;
import com.kasprzak.kamil.CarRentalSystem.enums.ReservationStatus;
import com.kasprzak.kamil.CarRentalSystem.exceptions.CarNotAvailableException;
import com.kasprzak.kamil.CarRentalSystem.exceptions.ReservationNotFoundException;
import com.kasprzak.kamil.CarRentalSystem.mappers.ToCarDTOMapper;
import com.kasprzak.kamil.CarRentalSystem.mappers.ToReservationResponseDTOMapper;
import com.kasprzak.kamil.CarRentalSystem.repositories.CarRepository;
import com.kasprzak.kamil.CarRentalSystem.repositories.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarRentalServiceImpl implements CarRentalService {

    private final CarRepository carRepository;
    private final ReservationRepository reservationRepository;
    private final ToReservationResponseDTOMapper toReservationResponseDTOMapper;
    private final ToCarDTOMapper toCarDTOMapper;


    @Override
    public boolean isCarAvailable(CarType type, LocalDateTime start, LocalDateTime end) {
        var blockingStatuses = List.of(ReservationStatus.NEW, ReservationStatus.IN_PROGRESS);
        return carRepository
                .findFirstAvailableCar(type, CarStatus.ACTIVE, start, end, blockingStatuses)
                .isPresent();
    }

    @Override
    public CarDTO addCar(CarType carType, String registration) {
        var car = Car
                .builder()
                .type(carType)
                .status(CarStatus.ACTIVE)
                .registration(registration)
                .build();

        Car savedCar = carRepository.save(car);

        return toCarDTOMapper.map(savedCar);
    }

    @Override
    @Transactional
    public ReservationResponseDTO reserveCar(final ReservationRequestDTO request) {

        var start = request.startDate();
        var end = start.plusDays(request.numberOfDays());

        var car = findAvailableCar(request.carType(), start, end);

        var reservation = Reservation
                .builder()
                .car(car)
                .startDate(start)
                .endDate(end)
                .status(ReservationStatus.NEW)
                .clientName(request.clientName())
                .build();

        reservationRepository.save(reservation);

        return toReservationResponseDTOMapper.map(reservation);

    }

    @Override
    @Transactional
    public ReservationResponseDTO updateReservationStatus(Long reservationId, ReservationStatus newStatus) {

        var reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation with id " + reservationId + " not found"));

        if (!isValidStatusTransition(reservation.getStatus(), newStatus)) {
            throw new IllegalArgumentException(
                    "Invalid status transition: " + reservation.getStatus() + " -> " + newStatus);
        }

        reservation.setStatus(newStatus);
        reservationRepository.save(reservation);
        return toReservationResponseDTOMapper.map(reservation);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReservationResponseDTO> getAllReservation(Pageable pageable){
        return reservationRepository.findAll(pageable).map(toReservationResponseDTOMapper::map);
    }

    @Override
    public Page<CarDTO> getAllCars(Pageable pageable){
        return carRepository.findAll(pageable).map(toCarDTOMapper::map);
    }

    protected boolean isValidStatusTransition(ReservationStatus current, ReservationStatus next) {
        return switch (current) {
            case NEW -> next == ReservationStatus.IN_PROGRESS || next == ReservationStatus.CANCELLED;
            case IN_PROGRESS -> next == ReservationStatus.RETURNED;
            case RETURNED, CANCELLED -> false;
        };
    }

    protected Car findAvailableCar(CarType type, LocalDateTime start, LocalDateTime end) {
        var blockingStatuses = List.of(ReservationStatus.NEW, ReservationStatus.IN_PROGRESS);
        return carRepository
                .findFirstAvailableCar(type, CarStatus.ACTIVE, start, end, blockingStatuses)
                .orElseThrow(
                        () -> new CarNotAvailableException("Car type " + type + " not available from " + start + " to " + end));
    }
}
