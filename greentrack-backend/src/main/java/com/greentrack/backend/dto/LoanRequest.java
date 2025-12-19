package com.greentrack.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoanRequest {
    @NotNull(message = "El ID del empleado es obligatorio")
    private Long userId;

    @NotNull(message = "El ID del equipo es obligatorio")
    private Long equipmentId;
}