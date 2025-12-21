package com.greentrack.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "equipos")
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", unique = true, nullable = false)
    private String name;

    @Column(name = "tipo")
    private String type;

    @Column(name = "marca")
    private String brand;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private Status status = Status.DISPONIBLE;

    public enum Status {
        DISPONIBLE, PRESTADO
    }
}
