package com.greentrack.backend.repository;

import com.greentrack.backend.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query("SELECT l FROM Loan l WHERE " +
            "(:userId IS NULL OR l.user.id = :userId) AND " +
            "(:equipmentId IS NULL OR l.equipment.id = :equipmentId)")
    List<Loan> findByFilters(@Param("userId") Long userId,
                             @Param("equipmentId") Long equipmentId);

    // validacion de estado
    boolean existsByEquipmentIdAndStatus(Long equipmentId, Loan.LoanStatus status);

    // filtrar por ID de empleado
    List<Loan> findByUserId(Long userId);

    // filtrar por rango de fechas (AGREGAMOS @Param AQUÍ PARA EVITAR ERRORES)
    @Query("SELECT l FROM Loan l WHERE l.loanDate BETWEEN :startDate AND :endDate")
    List<Loan> findByDateRange(@Param("startDate") LocalDate startDate,
                               @Param("endDate") LocalDate endDate);
}