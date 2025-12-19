package com.greentrack.backend.repository;

import com.greentrack.backend.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    // por nombre de usuario o por ID de equipo
    @Query("SELECT l FROM Loan l WHERE " +
            "(:userId IS NULL OR l.user.id = :userId) AND " +
            "(:equipmentId IS NULL OR l.equipment.id = :equipmentId)")
    List<Loan> findByFilters(@Param("userId") Long userId,
                             @Param("equipmentId") Long equipmentId);

    // si un equipo tiene prestamos activos
    boolean existsByEquipmentIdAndStatus(Long equipmentId, Loan.LoanStatus status);
}
