package com.greentrack.backend.dto;

public class StatsDTO {
    public long totalEquipos;
    public long disponibles;
    public long prestados;

    public StatsDTO(long total, long disp, long prest) {
        this.totalEquipos = total;
        this.disponibles = disp;
        this.prestados = prest;
    }
}