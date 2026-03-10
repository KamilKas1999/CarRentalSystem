package com.kasprzak.kamil.CarRentalSystem.entity;

import com.kasprzak.kamil.CarRentalSystem.enums.CarStatus;
import com.kasprzak.kamil.CarRentalSystem.enums.CarType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cars")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CarType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CarStatus status;

    @Column(nullable = false)
    private String registration;
}
