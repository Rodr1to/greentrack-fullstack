package com.greentrack.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "prestamos")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "authorities", "password", "username", "enabled", "accountNonExpired", "accountNonLocked", "credentialsNonExpired"})
    private User user;

    @ManyToOne
    @JoinColumn(name = "equipo_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Equipment equipment;

    @Column(name = "fecha_prestamo", nullable = false)
    private LocalDate loanDate;

    @Column(name = "fecha_devolucion")
    private LocalDate returnDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private LoanStatus status = LoanStatus.ACTIVO;

    public enum LoanStatus {
        ACTIVO, DEVUELTO
    }
}