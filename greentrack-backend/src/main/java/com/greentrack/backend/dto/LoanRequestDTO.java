package com.greentrack.backend.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class LoanRequestDTO {
    private Long userId;
    private Long equipmentId;
    private LocalDate loanDate;
}