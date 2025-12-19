package com.greentrack.backend.controller;

import com.greentrack.backend.dto.StatsDTO;
import com.greentrack.backend.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin(origins = "http://localhost:4200")
public class DashboardController {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @GetMapping("/stats")
    public StatsDTO getStats(@RequestParam(required = false) String brand) {
        long total;
        long disponibles;
        long prestados;

        if (brand != null && !brand.isEmpty() && !brand.equals("TODOS")) {
            total = equipmentRepository.countByBrand(brand);
            disponibles = equipmentRepository.countByBrandAndStatus(brand, "AVAILABLE");
            prestados = total - disponibles;
        } else {
            total = equipmentRepository.count();
            disponibles = equipmentRepository.countByStatus("AVAILABLE");
            prestados = total - disponibles;
        }

        return new StatsDTO(total, disponibles, prestados);
    }
}
