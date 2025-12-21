package com.greentrack.backend.dto;

import com.greentrack.backend.entity.Loan;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class LoanDTO {
    private Long id;

    @NotNull(message = "El ID del equipo es obligatorio")
    private Long equipmentId;

    // para devolver info al frontend
    private String equipmentName;
    private String username;
    private LocalDate loanDate;
    private LocalDate returnDate;
    private String status;

    public LoanDTO(Loan loan) {
        this.id = loan.getId();
        this.loanDate = loan.getLoanDate();
        this.returnDate = loan.getReturnDate();

        if (loan.getEquipment() != null) {
            this.equipmentId = loan.getEquipment().getId();
            this.equipmentName = loan.getEquipment().getName();
        }

        if (loan.getUser() != null) {
            this.username = loan.getUser().getFullName();
        }

        if (loan.getStatus() != null) {
            this.status = loan.getStatus().name();
        }
    }
}
