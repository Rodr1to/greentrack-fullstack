package com.greentrack.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
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
}
