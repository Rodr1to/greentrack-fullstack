package com.greentrack.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockSummaryDTO {
    private String name;
    private Long total;
    private Long available; // disponibles
    private Long loaned; // prestados
}
