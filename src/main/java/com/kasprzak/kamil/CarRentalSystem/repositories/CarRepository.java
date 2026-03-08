package com.kasprzak.kamil.CarRentalSystem.repositories;

import com.kasprzak.kamil.CarRentalSystem.entity.Car;
import com.kasprzak.kamil.CarRentalSystem.enums.CarStatus;
import com.kasprzak.kamil.CarRentalSystem.enums.CarType;
import com.kasprzak.kamil.CarRentalSystem.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {

    List<Car> findByType(CarType type);

    List<Car> findByTypeAndStatus(CarType type, CarStatus status);

    @Query("""
        SELECT c
        FROM Car c
        WHERE c.type = :type
        AND c.status = :status
        AND NOT EXISTS (
            SELECT 1
            FROM Reservation r
            WHERE r.car = c
            AND r.status IN :blockingStatuses
            AND :start < r.endDate
            AND :end > r.startDate
        )
    """)
    Optional<Car> findFirstAvailableCar(
            CarType type,
            CarStatus status,
            LocalDateTime start,
            LocalDateTime end,
            List<ReservationStatus> blockingStatuses

    );
}
