package com.kasprzak.kamil.CarRentalSystem.repositories;

import com.kasprzak.kamil.CarRentalSystem.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {


}
